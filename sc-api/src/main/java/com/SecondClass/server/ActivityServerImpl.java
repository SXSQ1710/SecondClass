package com.SecondClass.server;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.SecondClass.entity.*;
import com.SecondClass.entity.Class;
import com.SecondClass.entity.R_entity.*;
import com.SecondClass.mapper.*;
import com.SecondClass.utils.DateUtils;
import com.SecondClass.utils.QrCodeUtils;
import com.SecondClass.utils.RedisIdWorker;
import com.SecondClass.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.Transient;
import java.util.*;
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
    OrganizationAppShiMapper organizationAppShiMapper;
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
            ActivityApplication activityApplication = redisUtils.queryForValue(RedisKeyName.ACTIVITY_APPLICATION, aAppIdStr,ActivityApplication.class,5L, TimeUnit.MINUTES, true,
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
                stringRedisTemplate.opsForValue().set(RedisKeyName.SECOND_KILL_STOCK + activity.getAid().toString(),activity.getALimittedNumber().toString());
                //2.4 缓存存入活动报名人员信息列表
                stringRedisTemplate.opsForZSet().add(RedisKeyName.SECOND_KILL_PARTICIPATION + activity.getAid().toString(),activity.getAname(),0);

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
        ActivityApplication activityApplication = redisUtils.queryForValue(RedisKeyName.ACTIVITY_APPLICATION, aAppIdStr,ActivityApplication.class,10L, TimeUnit.DAYS, true,
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
        Activity activity = redisUtils.queryForValue(RedisKeyName.ACTIVITY, aid.toString(), Activity.class, 10L, TimeUnit.DAYS, true,
                (id) -> {
                    QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("aid", aid);
                    return activityMapper.selectOne(queryWrapper);
        });

        return Response.success(ResponseStatus.ACTIVITY_QUERY_SUCCESS,activity);
    }

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    public Response register(Participation participation){
        try{
            //1.判断是否为合法用户
            //String uid = participation.getUid().toString(); //压力测试用
            String uid = (String) StpUtil.getLoginId();
            String aid = participation.getAid().toString();
            //2.判断活动是否正在申请的活动举办时间内,活动开始前15分钟结束后15分钟
            Activity activity = redisUtils.queryForValue(RedisKeyName.ACTIVITY, aid, Activity.class, 10L, TimeUnit.DAYS, true,
                    (id) -> {
                        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("aid", aid);
                        queryWrapper.eq("a_uid",uid);
                        return activityMapper.selectOne(queryWrapper);
                    });
            Date start = activity.getARegisterOpen();
            Date end = activity.getARegisterClose();

            if (!DateUtils.isEffectiveDate(start, end)){
                return Response.error(ResponseStatus.REGISTER_ACTIVITY_FAIL_4);
            }

            User userInfo = redisUtils.queryForValue(RedisKeyName.MANAGE_USER, uid, User.class, 60L, TimeUnit.DAYS, true,
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
                    aid, uid,String.valueOf(pid),"0"
            );
            //3. 判断结果是否为0
            int r = result.intValue();
            if (r != 0) {
                //2.1. 不为0，表示没有报名资格
                return Response.error(r == 1 ? ResponseStatus.REGISTER_ACTIVITY_FAIL_1 : ResponseStatus.REGISTER_ACTIVITY_FAIL_2);
            }

            return Response.success(ResponseStatus.SUCCESS, pid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(ResponseStatus.REGISTER_ACTIVITY_FAIL);
        }
    }

    @Value("${myConfig.serveUrl}")
    private String serveUrl;

    public Response getSignIn(Long aid, Long uid, Integer type) {
        try {
            Activity activity = redisUtils.queryForValue(RedisKeyName.ACTIVITY, aid.toString(), Activity.class, 10L, TimeUnit.DAYS, true,
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
//            Date start = activity.getAHoldStart();
//            Date end = activity.getAHoldEnd();
//            long startTime = start.getTime();
//            long endTime = end.getTime();
//            start.setTime(startTime - 1000*60*60*15);
//            end.setTime(endTime + 1000*60*60*15);
//
//            if (!DateUtils.isEffectiveDate(start, end)){
//                return Response.error(ResponseStatus.ACTIVITY_GET_SIGN_IN_FAIL_2);
//            }

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


    private static final DefaultRedisScript<Long> SIGNIN;

    static {
        SIGNIN = new DefaultRedisScript<>();
        SIGNIN.setLocation(new ClassPathResource("signIn.lua"));
        SIGNIN.setResultType(Long.class);
    }

    @Override
    @Transactional
    public Response signIn(R_SignIn signIn) {
        try {
            //1.从redis中查找临时密钥对应的活动id
            String uid = signIn.getUid().toString();
            String aid = stringRedisTemplate.opsForValue().get(RedisKeyName.ACTIVITY_GET_SIGN_IN + signIn.getUuid());
            if (StringUtil.isNullOrEmpty(aid)){
                return Response.error(ResponseStatus.ACTIVITY_SIGN_IN_FAIL_2);
            }else {

                //2. 执行lua脚本
                Long result = stringRedisTemplate.execute(
                        SIGNIN,
                        Collections.emptyList(),
                        aid, uid,"2"
                );

                //3. 判断结果是否为0
                int r = result.intValue();
                if (r != 0) {
                    //2.1. 不为0，表示没有报名记录
                    return Response.error(ResponseStatus.ACTIVITY_SIGN_IN_FAIL_1);
                }


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

    @Override
    public Response searchByName(String aname,Page page) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("aname",aname);
        Page selectPage = activityMapper.selectPage(page, queryWrapper);
        return Response.success(ResponseStatus.ACTIVITY_APP_QUERY_SUCCESS,selectPage);
    }

    /**
     * @param uid
     * @param page
     * @return
     */
    @Override
    public Response getParticipationByUid(String uid,Page page) {
        QueryWrapper<Participation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        Page selectPage = participationMapper.selectPage(page, queryWrapper);
        List records = selectPage.getRecords();
        // 返回的数组
        List<R_Activity_Participation> list = new LinkedList<>();
        for (int i = 0; i < records.size(); i++) {
            Participation participation = (Participation) records.get(i);
            //1.通过参与表查看报名人员  根据uid查询到所有aid
            //1.1先用uid查询redis得到cid
            Long aid = participation.getAid();
            Activity activity;
            Object activityStr = stringRedisTemplate.opsForHash().get(RedisKeyName.ACTIVITY, aid.toString());
            //1.2 如果redis没有，从数据库查询
            if( activityStr != null){
                System.out.println("从redis中查询");
                activity = JSONUtil.toBean(activityStr.toString(), Activity.class);
            }else{

                QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
                activityQueryWrapper.eq("aid",aid);
                System.out.println("从数据库中查询");
                activity = activityMapper.selectOne(activityQueryWrapper);
                //将数据库查询结果存入redis
                stringRedisTemplate.opsForHash().put(RedisKeyName.ACTIVITY,aid.toString(),JSONUtil.toJsonStr(activity));
            }


            R_Activity_Participation activity_participation = R_Activity_Participation.builder().uid(uid)
                    .aid(activity.getAid())
                    .participateStatus(participation.getParticipateStatus())
                    .aname(activity.getAname())
                    .adescription(activity.getAdescription())
                    .aRegisterOpen(activity.getARegisterOpen())
                    .aRegisterClose(activity.getARegisterClose())
                    .aHoldStart(activity.getAHoldStart())
                    .aHoldEnd(activity.getAHoldEnd())
                    .apic(activity.getApic())
                    .aShichangNum(activity.getAShichangNum())
                    .aShichangType(activity.getAShichangType()).build();

            list.add(activity_participation);
        }
        page.setRecords(list);
        return Response.success(ResponseStatus.SUCCESS,page);
    }


    @Override
    public Response sendInfoToOrganization(R_ShiAppInfo info) {
        //1.获得具体某场活动的Aid来判断时长类型
        //1.1 通过aid查询活动
        Long aid = info.getAid();

        Activity activity = redisUtils.queryForValue(RedisKeyName.ACTIVITY, aid.toString(),Activity.class,5L, TimeUnit.MINUTES, true,
                (id) -> {
                    QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("aid", aid);
                    return activityMapper.selectOne(queryWrapper);
                });

        if(activity == null) throw new IllegalArgumentException();

        //1。2 获得市场类型
        Long sid = activity.getAShichangType();

        //2.存入组织申请时长表

        Long uid = Long.valueOf((String) StpUtil.getLoginId());
        OrganizationAppShi  organizationAppShi = OrganizationAppShi.builder().uid(uid)
                .sid(sid)
                .shiAppDescription(info.getShiAppDescription())
                .shiAppAttachment(info.getShiAppAttachment())
                .shiAppStatus(0).build();


        int i = organizationAppShiMapper.insert(organizationAppShi);

        if(i != 1) throw new IllegalArgumentException();

        return Response.success(ResponseStatus.SUBMIT_SHIAPPINFO_SUCCESS);
    }
}
