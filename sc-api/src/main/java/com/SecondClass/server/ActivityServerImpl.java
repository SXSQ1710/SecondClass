package com.SecondClass.server;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.SecondClass.entity.*;
import com.SecondClass.entity.Class;
import com.SecondClass.entity.R_entity.R_ActivityApplication;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.R_entity.R_Student;
import com.SecondClass.mapper.*;
import com.SecondClass.utils.DateUtils;
import com.SecondClass.utils.QrCodeUtils;
import com.SecondClass.utils.RedisIdWorker;
import com.SecondClass.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.RedisCommandExecutionException;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.plaf.IconUIResource;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ActivityServerImpl implements IActivityServer{
    @Resource
    ActivityMapper activityMapper;
    @Resource
    ActivityApplicationMapper activityApplicationMapper;
    @Resource
    ParticipationMapper participationMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ClassMapper classMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    RedisUtils redisUtils;
    @Resource
    RedisIdWorker redisIdWorker;

    @Transactional
    public Response applyActivity(R_ActivityApplication request){
        try{
            //1.生成活动描述
            //  将活动信息转换为json字符串存入申请表的活动描述
            Activity activity = BeanUtil.copyProperties(request, Activity.class);
            //System.out.println(activity);
            String jsonStr_activity = JSONUtil.toJsonStr(activity);

            //2.添加活动申请表到数据库
            //  设置申请状态为1
            ActivityApplication activityApplication = ActivityApplication.builder().uid(activity.getAUid())
                    .aAppDescription(jsonStr_activity)
                    .aAppAttachment(request.getAAppAttachment())
                    .aAppStatus(1).build();
            System.out.println(activityApplication);
            //  插入失败抛出异常
            if (activityApplicationMapper.insert(activityApplication) != 1) throw new IllegalArgumentException();

            //将活动申请活动信息写入redis，以活动id作为hashKey
            redisUtils.setValue(RedisKeyName.ACTIVITY_APPLICATION + activityApplication.getAAppId().toString(), activityApplication, 10L, TimeUnit.DAYS);
            return Response.success(ResponseStatus.ACTIVITY_APPLY_SUCCESS);
        }catch (IllegalArgumentException i){
            //redis插入失败
            i.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_APPLY_FAIL);
        }catch (DataIntegrityViolationException d){
            //数据库插入失败
            d.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_APPLY_FAIL);
        }
        catch (Exception e){
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }

    @Transactional
    public Response auditActivity(Integer aAppId,Integer status,String explain) {
        try {
            //1.更新活动申请实体和活动实体的状态
            //1.1 获得活动实体
            String aAppIdStr = aAppId.toString();
            ActivityApplication activityApplication = redisUtils.queryForValue(RedisKeyName.ACTIVITY_APPLICATION, aAppIdStr,ActivityApplication.class,5L, TimeUnit.MINUTES,
                    (id) -> {
                        QueryWrapper<ActivityApplication> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("a_app_id", aAppIdStr);
                        return activityApplicationMapper.selectOne(queryWrapper);
            });

            if (activityApplication == null) throw new IllegalArgumentException();
            //1.2修改活动申请状态   2表示通过 0：表示拒绝
            activityApplication.setAAppStatus(status);
            if(explain != null){
                activityApplication.setAAppExplain(explain);
            }else{
                activityApplication.setAAppExplain("无");
            }

            //2.如果活动通过的话，将活动存入活动表中
            if(status == 2){
                //2.1添加活动到活动表 并将状态设置为1（活动审核中）
                String json = activityApplication.getAAppDescription();
                Activity activity = JSONUtil.toBean(json, Activity.class);
                //2.2设置活动状态：审核通过 2
                activity.setAstatus(status);
                if(activityMapper.insert(activity) !=1)throw new IllegalArgumentException();
                //2.2 缓存存入活动表中
                redisUtils.setValue(RedisKeyName.ACTIVITY + activity.getAid().toString(),activity,10L, TimeUnit.DAYS);
                //2.3 缓存存入活动报名限制人数
                stringRedisTemplate.opsForValue().set(RedisKeyName.SECOND_KILL + activity.getAid().toString(),activity.getALimittedNumber().toString());

                //2.4 新的活动状态(多了一个aid）更新到活动描述中
                String jsonStr_activity = JSONUtil.toJsonStr(activity);
                activityApplication.setAAppDescription(jsonStr_activity);

            }
            //2.5 更新活动申请表
            if(activityApplicationMapper.updateById(activityApplication)!=1)throw new IllegalArgumentException();

            //3.修改redis里面的数据
            //3.1 活动申请表
            stringRedisTemplate.delete(RedisKeyName.ACTIVITY_APPLICATION + activityApplication.getAAppId().toString());

            return Response.success(ResponseStatus.ADUIT_SUCCESS);
        } catch (IllegalArgumentException i) {
            i.printStackTrace();
            return Response.error(ResponseStatus.ADUIT_FAIL);
        } catch (DataIntegrityViolationException d) {
            d.printStackTrace();
            return Response.error(ResponseStatus.ADUIT_FAIL);
        } catch (Exception e) {
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }


    public Response findActivityAppByUid(Long uid,Page page) {
        QueryWrapper<ActivityApplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        IPage<ActivityApplication> appList =  activityApplicationMapper.selectPage(page,queryWrapper);
        if(appList.getSize() == 0 ){
            return Response.success(ResponseStatus.ACTIVITY_APP_QUERY_FAIL,appList);
        }
        else{
            return Response.success(ResponseStatus.ACTIVITY_APP_QUERY_SUCCESS,appList);
        }
    }


    public Response findAppStatusByAid(Long aAppId) {
        String aAppIdStr = aAppId.toString();
        ActivityApplication activityApplication = redisUtils.queryForValue(RedisKeyName.ACTIVITY_APPLICATION, aAppIdStr,ActivityApplication.class,10L, TimeUnit.DAYS,
                (id) -> {
                    QueryWrapper<ActivityApplication> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("a_app_id", aAppIdStr);
                    return activityApplicationMapper.selectOne(queryWrapper);
        });

        if(activityApplication == null){
            return Response.success(ResponseStatus.ACTIVITY_APP_QUERY_FAIL,activityApplication);
        }
        else{
            return Response.success(ResponseStatus.ACTIVITY_APP_QUERY_SUCCESS,activityApplication);
        }
    }

    @Override
    public Response getAllApp(Page page) {
        QueryWrapper<ActivityApplication> queryWrapper = new QueryWrapper<>();
        IPage<ActivityApplication> appList =  activityApplicationMapper.selectPage(page,queryWrapper);
        return Response.success(ResponseStatus.ACTIVITY_APP_QUERY_SUCCESS,appList);
    }

    @Override
    public Response getAll(Page page) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        IPage<Activity> activityIPage =  activityMapper.selectPage(page,queryWrapper);

        return Response.success(ResponseStatus.ACTIVITY_APP_QUERY_SUCCESS,activityIPage);

    }


    public Response findActivityByAid(Long aid) {
        Activity activity = redisUtils.queryForValue(RedisKeyName.ACTIVITY, aid.toString(), Activity.class, 10L, TimeUnit.DAYS,
                (id) -> {
                    QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("aid", aid);
                    return activityMapper.selectOne(queryWrapper);
        });

        return Response.success(ResponseStatus.ACTIVITY_QUERY_SUCCESS,activity);
    }

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    private static final ExecutorService SECKILL_EXECUTOR = Executors.newSingleThreadExecutor();

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    @PostConstruct
    private void init(){
        SECKILL_EXECUTOR.submit(new VoucherParticipationHandler());
        if (!stringRedisTemplate.hasKey("stream.participation")){
            stringRedisTemplate.opsForStream().createGroup("stream.participation", "g1");
        }
    }

    private class VoucherParticipationHandler implements Runnable{
        String queueName = "stream.participation" ;

        @Override
        public void run() {
            while (true){
                try{
                    //1.获取消息队列中的报名信息
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
                            StreamOffset.create(queueName, ReadOffset.lastConsumed())
                    );
                    //2.判断消息获取是否成功
                    if (list == null || list.isEmpty()){
                        //2.1如果为空表示消息队列中没有消息，继续下一个循环
                        continue;
                    }
                    //3.解析数据
                    MapRecord<String, Object, Object> record = list.get(0);
                    Map<Object, Object> value = record.getValue();
                    Participation participation = BeanUtil.fillBeanWithMap(value, new Participation(), true);
                    //4.写入数据库
                    saveParticipation(participation);
                    //5.ack确认
                    stringRedisTemplate.opsForStream().acknowledge(queueName,"g1",record.getId());
                }catch (Exception e){
                    log.error("处理PendingList报名异常", e);
                    handlePendingList();
                }
            }
        }

        private void handlePendingList(){
            while (true){
                try{
                    //1.获取消息队列中的报名信息
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),
                            StreamReadOptions.empty().count(1),
                            StreamOffset.create(queueName, ReadOffset.from("0"))
                    );
                    //2.判断消息获取是否成功
                    if (list == null || list.isEmpty()){
                        //2.1如果为空表示PendingList中没有异常消息了，继续下一个循环
                        break;
                    }
                    //3.解析数据
                    MapRecord<String, Object, Object> record = list.get(0);
                    Map<Object, Object> value = record.getValue();
                    Participation participation = BeanUtil.fillBeanWithMap(value, new Participation(), true);
                    //4.写入数据库
                    saveParticipation(participation);
                    //5.ack确认
                    stringRedisTemplate.opsForStream().acknowledge(queueName,"g1",record.getId());
                }catch (Exception e){
                    log.error("处理PendingList报名异常", e);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        }

        private void saveParticipation(Participation participation){
            participation.setParticipateStatus(0);
            if(participationMapper.insert(participation) != 1) throw new IllegalArgumentException();
        }
    }



    public Response register(Participation participation){
        try{//1.判断是否为合法用户
            String uid = participation.getUid().toString();
            User userInfo = redisUtils.queryForValue(RedisKeyName.MANAGE_USER, uid, User.class, 60L, TimeUnit.DAYS,
                    (id) -> {
                        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("uid", uid);
                        return userMapper.selectOne(queryWrapper);
                    });
            if (!BeanUtil.isNotEmpty(userInfo)) return Response.error(ResponseStatus.REGISTER_ACTIVITY_FAIL_3);
            //2. 执行lua脚本
            long pid = redisIdWorker.nextId("participation");
            Long result = stringRedisTemplate.execute(
                    SECKILL_SCRIPT,
                    Collections.emptyList(),
                    participation.getAid().toString(), participation.getUid().toString(),String.valueOf(pid)
            );
            //3. 判断结果是否为0
            int r = result.intValue();
            if (r != 0) {
                //2.1. 不为0，表示没有报名资格
                return Response.error(r == 1 ? ResponseStatus.REGISTER_ACTIVITY_FAIL_1 : ResponseStatus.REGISTER_ACTIVITY_FAIL_2);
            }

            return Response.success(ResponseStatus.SUCCESS, pid);
        }catch (Exception e){
            return Response.error(ResponseStatus.REGISTER_ACTIVITY_FAIL);
        }

//        try {
//            String UUID = java.util.UUID.randomUUID().toString().replaceAll("-", "");
//            participation.setPid(UUID);
//            participation.setParticipateStatus(0);
//            if(participationMapper.insert(participation) != 1) throw new IllegalArgumentException();
//
//            return Response.success(ResponseStatus.REGISTER_ACTIVITY_SUCCESS);
//        } catch (IllegalArgumentException i) {
//            i.printStackTrace();
//            return Response.error(ResponseStatus.REGISTER_ACTIVITY_FAIL);
//        } catch (DataIntegrityViolationException d) {
//            d.printStackTrace();
//            return Response.error(ResponseStatus.REGISTER_ACTIVITY_FAIL);
//        } catch (Exception e) {
//            //其他错误
//            e.printStackTrace();
//            return Response.success(ResponseStatus.ERROR);
//        }

    }

    @Value("${myConfig.serveUrl}")
    private String serveUrl;

    public Response getSignIn(Long aid, Long uid, Integer type) {
        try {
            Activity activity = redisUtils.queryForValue(RedisKeyName.ACTIVITY, aid.toString(), Activity.class, 10L, TimeUnit.DAYS,
                    (id) -> {
                        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("aid", aid);
                        queryWrapper.eq("a_uid",uid);
                        return activityMapper.selectOne(queryWrapper);
            });
            if (!Objects.equals(activity.getAUid(), uid)) throw new IllegalArgumentException();
            //判断活动状态是否为2(活动审核通过)
            if (activity.getAstatus() != 2) return Response.error(ResponseStatus.ACTIVITY_GET_SIGN_IN_FAIL_3);
            //判断活动是否正在申请的活动举办时间内,活动开始前15分钟结束后15分钟
            Date start = activity.getAHoldStart();
            Date end = activity.getAHoldEnd();
            long startTime = start.getTime();
            long endTime = end.getTime();
            start.setTime(startTime - 1000*60*60*15);
            end.setTime(endTime + 1000*60*60*15);

            if (!DateUtils.isEffectiveDate(start, end)){
                return Response.error(ResponseStatus.ACTIVITY_GET_SIGN_IN_FAIL_2);
            }

            if(BeanUtil.isNotEmpty(activity)){
                //1.生成随机uuid保存在redis里并设置5分钟的TTL
                String uuid = UUID.randomUUID().toString();
                String signInUrl;
                if (type == 1){
                    stringRedisTemplate.opsForValue().set(RedisKeyName.ACTIVITY_GET_SIGN_IN+uuid,aid.toString());
                    stringRedisTemplate.expire(RedisKeyName.ACTIVITY_GET_SIGN_IN+uuid,5L, TimeUnit.MINUTES);
                    signInUrl = serveUrl + "/api/activity/signIn/" + uuid;
                } else if (type == 0) {
                    stringRedisTemplate.opsForValue().set(RedisKeyName.ACTIVITY_GET_SIGN_OFF+uuid,aid.toString());
                    stringRedisTemplate.expire(RedisKeyName.ACTIVITY_GET_SIGN_OFF+uuid,5L, TimeUnit.MINUTES);
                    //2.签到扫描二维码的访问路径
                    signInUrl = serveUrl + "/api/activity/signOff/" + uuid;
                } else {
                    return Response.error(ResponseStatus.ACTIVITY_GET_SIGN_IN_FAIL_1);
                }

                //3.生成二维码以base64返回响应
                String qrCode = QrCodeUtils.createQRCode(signInUrl);
                return Response.success(ResponseStatus.ACTIVITY_GET_SIGN_IN_SUCCESS,qrCode);
            }else {
                return Response.error(ResponseStatus.ACTIVITY_GET_SIGN_IN_FAIL_1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_GET_SIGN_IN_FAIL_1);
        }
    }

    @Override
    public Response signIn(R_SignIn signIn) {
        try {
            String aid = stringRedisTemplate.opsForValue().get(RedisKeyName.ACTIVITY_GET_SIGN_IN + signIn.getUuid());
            if (StringUtil.isNullOrEmpty(aid)){
                throw new IllegalArgumentException();
            }else {
                Participation participation = BeanUtil.copyProperties(signIn, Participation.class);
                UpdateWrapper<Participation> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("aid",aid).eq("uid",participation.getUid());
                updateWrapper.set("participate_status",2);
                if (participationMapper.update(null,updateWrapper) != 1) throw new IllegalArgumentException();
                return Response.success(ResponseStatus.ACTIVITY_SIGN_IN_SUCCESS);
            }
        } catch (IllegalArgumentException i) {
            i.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_SIGN_IN_FAIL);
        } catch (DataIntegrityViolationException d) {
            d.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_SIGN_IN_FAIL);
        } catch (Exception e) {
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);

        }
    }

    public Response signOff(Participation participation) {
        try {
            UpdateWrapper<Participation> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("aid",participation.getAid()).eq("uid",participation.getUid());
            updateWrapper.set("participate_status",3);
            if (participationMapper.update(null,updateWrapper) != 1) throw new IllegalArgumentException();
            return Response.success(ResponseStatus.ACTIVITY_SIGN_OFF_SUCCESS);
        } catch (IllegalArgumentException i) {
            i.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_SIGN_OFF_FAIL);
        } catch (DataIntegrityViolationException d) {
            d.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_SIGN_OFF_FAIL);
        } catch (Exception e) {
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);

        }
    }

    /**
     *
     * @param aid
     * @param page
     * @return
     */
    @Override
    public Response getAllRegisteredUser(Long aid, Page page) {
        QueryWrapper<Participation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("aid",aid);
        queryWrapper.eq("participate_status",0);
        Page selectPage = participationMapper.selectPage(page, queryWrapper);
        List records = selectPage.getRecords();

        List<R_Student> list = new LinkedList<>();
        for (int i = 0; i < records.size(); i++) {
            Participation participation = (Participation) records.get(i);
            //1.通过参与表查看报名人员  得到uid后再查询cid
            //1.1先用uid查询redis得到cid
            Long uid = participation.getUid();
            User user;
            Object userStr = stringRedisTemplate.opsForHash().get("secondclass:user:userList", uid.toString());
            //1.2 如果redis没有，从数据库查询
            if( userStr != null){
                System.out.println("从redis中查询");
                user = JSONUtil.toBean(userStr.toString(), User.class);
            }else{

                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("uid",uid);
                System.out.println("从数据库中查询");
                user = userMapper.selectOne(userQueryWrapper);
                //将数据库查询结果存入redis
                stringRedisTemplate.opsForHash().put("secondclass:user:userList",uid.toString(),JSONUtil.toJsonStr(user));
            }

            //2.通过cid查询班级、校区等信息
            //2.1用cid查询redis中班级信息
            Long cid = user.getCid();
            Class aClass;
            //2.2 从redis中查询,如果没有的话就查询数据库

            String classJson = (String) stringRedisTemplate.opsForHash().get("secondclass:class:classList", cid.toString());
            if(classJson != null){
                System.out.println("从redis中查询");
                aClass = JSONUtil.toBean(classJson, Class.class);
            }else {
                QueryWrapper<Class> classQueryWrapper = new QueryWrapper<>();
                classQueryWrapper.eq("cid",cid);
                System.out.println("从数据库中查询");
                aClass = classMapper.selectOne(classQueryWrapper);
                stringRedisTemplate.opsForHash().put("secondclass:class:classList",cid.toString(),JSONUtil.toJsonStr(aClass));
            }
            R_Student student = R_Student.builder().uid(user.getUid())
                    .uname(user.getUname())
                    .phone(user.getPhone())
                    .cname(aClass.getCname())
                    .grade(aClass.getGrade())
                    .major(aClass.getMajor())
                    .college(aClass.getCollege())
                    .campus(aClass.getCampus()).build();

            list.add(student);
        }
        page.setRecords(list);
        return Response.success(ResponseStatus.SUCCESS,page);
    }


    @Override
    public Response modifyRegisterStatusByUid(Participation participation) {
        try {
            //审核通过
            UpdateWrapper<Participation> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("aid",participation.getAid()).eq("uid",participation.getUid());
            updateWrapper.set("participate_status",participation.getParticipateStatus());
            if (participationMapper.update(null,updateWrapper) != 1) throw new IllegalArgumentException();
            return Response.success(ResponseStatus.ADUIT_SUCCESS);
        } catch (IllegalArgumentException i) {
            i.printStackTrace();
            return Response.error(ResponseStatus.ADUIT_FAIL);
        } catch (DataIntegrityViolationException d) {
            d.printStackTrace();
            return Response.error(ResponseStatus.ADUIT_FAIL);
        } catch (Exception e) {
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }

    @Override
    public Response getAllParticipatedMember(Long aid) {
        QueryWrapper<Participation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("aid",aid);
        queryWrapper.eq("participate_status",3);
        List<Participation> records = participationMapper.selectList(queryWrapper);

        List<R_Student> list = new LinkedList<>();

        for (int i = 0; i < records.size(); i++) {
            Participation participation = (Participation) records.get(i);
            //1.通过参与表查看报名人员  得到uid后再查询cid
            //1.1先用uid查询redis得到cid
            Long uid = participation.getUid();
            User user;
            Object userStr = stringRedisTemplate.opsForHash().get("secondclass:user:userList", uid.toString());
            //1.2 如果redis没有，从数据库查询
            if( userStr != null){
                System.out.println("从redis中查询");
                user = JSONUtil.toBean(userStr.toString(), User.class);
            }else{

                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("uid",uid);
                System.out.println("从数据库中查询");
                user = userMapper.selectOne(userQueryWrapper);
                //将数据库查询结果存入redis
                stringRedisTemplate.opsForHash().put("secondclass:user:userList",uid.toString(),JSONUtil.toJsonStr(user));
            }

            //2.通过cid查询班级、校区等信息
            //2.1用cid查询redis中班级信息
            Long cid = user.getCid();
            Class aClass;
            //2.2 从redis中查询,如果没有的话就查询数据库

            String classJson = (String) stringRedisTemplate.opsForHash().get("secondclass:class:classList", cid.toString());
            if(classJson != null){
                System.out.println("从redis中查询");
                aClass = JSONUtil.toBean(classJson, Class.class);
            }else {
                QueryWrapper<Class> classQueryWrapper = new QueryWrapper<>();
                classQueryWrapper.eq("cid",cid);
                System.out.println("从数据库中查询");
                aClass = classMapper.selectOne(classQueryWrapper);
                stringRedisTemplate.opsForHash().put("secondclass:class:classList",cid.toString(),JSONUtil.toJsonStr(aClass));
            }
            R_Student student = R_Student.builder().uid(user.getUid())
                    .uname(user.getUname())
                    .phone(user.getPhone())
                    .cname(aClass.getCname())
                    .grade(aClass.getGrade())
                    .major(aClass.getMajor())
                    .college(aClass.getCollege())
                    .campus(aClass.getCampus()).build();

            list.add(student);
        }
        return Response.success(ResponseStatus.SUCCESS,list);
    }
}
