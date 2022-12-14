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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
            //1.??????????????????
            //  ????????????????????????json???????????????????????????????????????
            Activity activity = BeanUtil.copyProperties(request, Activity.class);
            //System.out.println(activity);
            String jsonStr_activity = JSONUtil.toJsonStr(activity);

            //2.?????????????????????????????????
            //  ?????????????????????1
            ActivityApplication activityApplication = ActivityApplication.builder().uid(activity.getAUid())
                    .aAppDescription(jsonStr_activity)
                    .aAppAttachment(request.getAAppAttachment())
                    .aAppStatus(1).build();
            System.out.println(activityApplication);
            //  ????????????????????????
            if (activityApplicationMapper.insert(activityApplication) != 1) throw new IllegalArgumentException();

            //?????????????????????????????????redis????????????id??????hashKey
            redisUtils.setValue(RedisKeyName.ACTIVITY_APPLICATION + activityApplication.getAAppId().toString(), activityApplication, 10L, TimeUnit.DAYS);
            return Response.success(ResponseStatus.ACTIVITY_APPLY_SUCCESS);
        }catch (IllegalArgumentException i){
            //redis????????????
            i.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_APPLY_FAIL);
        }catch (DataIntegrityViolationException d){
            //?????????????????????
            d.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_APPLY_FAIL);
        }
        catch (Exception e){
            //????????????
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }

    @Transactional
    public Response auditActivity(Integer aAppId,Integer status,String explain) {
        try {
            //1.????????????????????????????????????????????????
            //1.1 ??????????????????
            String aAppIdStr = aAppId.toString();
            ActivityApplication activityApplication = redisUtils.queryForValue(RedisKeyName.ACTIVITY_APPLICATION, aAppIdStr,ActivityApplication.class,5L, TimeUnit.MINUTES, true,
                    (id) -> {
                        QueryWrapper<ActivityApplication> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("a_app_id", aAppIdStr);
                        return activityApplicationMapper.selectOne(queryWrapper);
            });

            if (activityApplication == null) throw new IllegalArgumentException();
            //1.2????????????????????????   2???????????? 0???????????????
            activityApplication.setAAppStatus(status);
            if(explain != null){
                activityApplication.setAAppExplain(explain);
            }else{
                activityApplication.setAAppExplain("???");
            }

            //2.??????????????????????????????????????????????????????
            if(status == 2){
                //2.1???????????????????????? ?????????????????????1?????????????????????
                String json = activityApplication.getAAppDescription();
                Activity activity = JSONUtil.toBean(json, Activity.class);
                //2.2????????????????????????????????? 2
                activity.setAstatus(status);
                if(activityMapper.insert(activity) !=1)throw new IllegalArgumentException();
                //2.2 ????????????????????????
                redisUtils.setValue(RedisKeyName.ACTIVITY + activity.getAid().toString(),activity,10L, TimeUnit.DAYS);
                //2.3 ????????????????????????????????????
                stringRedisTemplate.opsForValue().set(RedisKeyName.SECOND_KILL_STOCK + activity.getAid().toString(),activity.getALimittedNumber().toString());
                //2.4 ??????????????????????????????????????????
                stringRedisTemplate.opsForZSet().add(RedisKeyName.SECOND_KILL_PARTICIPATION + activity.getAid().toString(),activity.getAname(),0);

                //2.4 ??????????????????(????????????aid???????????????????????????
                String jsonStr_activity = JSONUtil.toJsonStr(activity);
                activityApplication.setAAppDescription(jsonStr_activity);

            }
            //2.5 ?????????????????????
            if(activityApplicationMapper.updateById(activityApplication)!=1)throw new IllegalArgumentException();

            //3.??????redis???????????????
            //3.1 ???????????????
            stringRedisTemplate.delete(RedisKeyName.ACTIVITY_APPLICATION + activityApplication.getAAppId().toString());

            return Response.success(ResponseStatus.ADUIT_SUCCESS);
        } catch (IllegalArgumentException i) {
            i.printStackTrace();
            return Response.error(ResponseStatus.ADUIT_FAIL);
        } catch (DataIntegrityViolationException d) {
            d.printStackTrace();
            return Response.error(ResponseStatus.ADUIT_FAIL);
        } catch (Exception e) {
            //????????????
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }


    public Response findActivityAppByUid(Long uid,Page page) {
        //??????token????????????id????????????uid???????????????
        String user_id = StpUtil.getLoginId().toString();
        QueryWrapper<ActivityApplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",user_id);
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
            //1.???????????????????????????
            //String uid = participation.getUid().toString(); //???????????????
            String uid = (String) StpUtil.getLoginId();
            String aid = participation.getAid().toString();
            //2.??????????????????????????????????????????????????????,???????????????15???????????????15??????
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
            //2. ??????lua??????
            long pid = redisIdWorker.nextId("participation");
            Long result = stringRedisTemplate.execute(
                    SECKILL_SCRIPT,
                    Collections.emptyList(),
                    aid, uid,String.valueOf(pid),"0"
            );
            //3. ?????????????????????0
            int r = result.intValue();
            if (r != 0) {
                //2.1. ??????0???????????????????????????
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
            //???????????????????????????2(??????????????????)
            if (activity.getAstatus() != 2) return Response.error(ResponseStatus.ACTIVITY_GET_SIGN_IN_FAIL_3);
            //??????????????????????????????????????????????????????,???????????????15???????????????15??????
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
                //1.????????????uuid?????????redis????????????5?????????TTL
                String uuid = UUID.randomUUID().toString();
                String signInUrl;
                if (type == 1){
                    stringRedisTemplate.opsForValue().set(RedisKeyName.ACTIVITY_GET_SIGN_IN+uuid,aid.toString());
                    stringRedisTemplate.expire(RedisKeyName.ACTIVITY_GET_SIGN_IN+uuid,5L, TimeUnit.MINUTES);
                    signInUrl = serveUrl + "/api/activity/signIn/" + uuid;
                } else if (type == 0) {
                    stringRedisTemplate.opsForValue().set(RedisKeyName.ACTIVITY_GET_SIGN_OFF+uuid,aid.toString());
                    stringRedisTemplate.expire(RedisKeyName.ACTIVITY_GET_SIGN_OFF+uuid,5L, TimeUnit.MINUTES);
                    //2.????????????????????????????????????
                    signInUrl = serveUrl + "/api/activity/signOff/" + uuid;
                } else {
                    return Response.error(ResponseStatus.ACTIVITY_GET_SIGN_IN_FAIL_1);
                }

                //3.??????????????????base64????????????
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


    private static final DefaultRedisScript<Long> SIGN;

    static {
        SIGN = new DefaultRedisScript<>();
        SIGN.setLocation(new ClassPathResource("signIn.lua"));
        SIGN.setResultType(Long.class);
    }

    @Transactional
    public Response signInOrOff(R_SignIn signInfo) {
        try {
            //1.???redis????????????????????????????????????id
            String uid = signInfo.getUid().toString();
            String type = signInfo.getType();
            String aid;
            String oldStatus;
            String status;
            String typeStr;
            if (type.equals("1")){
                aid = stringRedisTemplate.opsForValue().get(RedisKeyName.ACTIVITY_GET_SIGN_IN + signInfo.getUuid());
                oldStatus = "1"; status = "2"; typeStr = "i";
            }else if(type.equals("0")){
                aid = stringRedisTemplate.opsForValue().get(RedisKeyName.ACTIVITY_GET_SIGN_OFF + signInfo.getUuid());
                oldStatus = "2"; status = "3"; typeStr = "o";
            }else {
                return Response.error(ResponseStatus.ACTIVITY_SIGN_FAIL);
            }

            if (StringUtil.isNullOrEmpty(aid)){
                return Response.error(ResponseStatus.ACTIVITY_SIGN_IN_FAIL_2);
            }else {

                //2. ??????lua??????
                Long result = stringRedisTemplate.execute(
                        SIGN,
                        Collections.emptyList(),
                        aid, uid, oldStatus, status, typeStr
                );

                //3. ?????????????????????0
                int r = result.intValue();
                if (r != 0) {
                    //2.1. ??????0???????????????????????????
                    return Response.error(ResponseStatus.ACTIVITY_SIGN_IN_FAIL_1);
                }


                return Response.success(type.equals("1") ? ResponseStatus.ACTIVITY_SIGN_IN_SUCCESS : ResponseStatus.ACTIVITY_SIGN_OFF_SUCCESS);
            }
        } catch (Exception e) {
            //????????????
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
            //1.?????????????????????????????????  ??????uid????????????cid
            //1.1??????uid??????redis??????cid
            Long uid = participation.getUid();
            User user;
            Object userStr = stringRedisTemplate.opsForHash().get("secondclass:user:userList", uid.toString());
            //1.2 ??????redis???????????????????????????
            if( userStr != null){
                System.out.println("???redis?????????");
                user = JSONUtil.toBean(userStr.toString(), User.class);
            }else{

                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("uid",uid);
                System.out.println("?????????????????????");
                user = userMapper.selectOne(userQueryWrapper);
                //??????????????????????????????redis
                stringRedisTemplate.opsForHash().put("secondclass:user:userList",uid.toString(),JSONUtil.toJsonStr(user));
            }

            //2.??????cid??????????????????????????????
            //2.1???cid??????redis???????????????
            Long cid = user.getCid();
            Class aClass;
            //2.2 ???redis?????????,????????????????????????????????????

            String classJson = (String) stringRedisTemplate.opsForHash().get("secondclass:class:classList", cid.toString());
            if(classJson != null){
                System.out.println("???redis?????????");
                aClass = JSONUtil.toBean(classJson, Class.class);
            }else {
                QueryWrapper<Class> classQueryWrapper = new QueryWrapper<>();
                classQueryWrapper.eq("cid",cid);
                System.out.println("?????????????????????");
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

    private static final DefaultRedisScript<Long> modifyRegisterStatusByUid;

    static {
        modifyRegisterStatusByUid = new DefaultRedisScript<>();
        modifyRegisterStatusByUid.setLocation(new ClassPathResource("modifyRegisterStatusByUid.lua"));
        modifyRegisterStatusByUid.setResultType(Long.class);
    }

    @Transactional
    public Response modifyRegisterStatusByUid(Participation participation) {
        try {
            //????????????
            String aid = participation.getAid().toString();
            String uid = participation.getUid().toString();
            String status = participation.getParticipateStatus().toString();
            UpdateWrapper<Participation> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("aid",aid).eq("uid",uid);
            updateWrapper.set("participate_status",status);
            if (participationMapper.update(null,updateWrapper) != 1) throw new IllegalArgumentException();

            //??????lua??????
            Long result = stringRedisTemplate.execute(
                    modifyRegisterStatusByUid,
                    Collections.emptyList(),
                    aid, uid,"1"
            );

            //3. ?????????????????????0
            int r = result.intValue();
            if (r != 0) {
                //2.1. ??????0???????????????????????????
                return Response.error(ResponseStatus.ADUIT_FAIL);
            }


            return Response.success(ResponseStatus.ADUIT_SUCCESS);
        } catch (IllegalArgumentException i) {
            i.printStackTrace();
            return Response.error(ResponseStatus.ADUIT_FAIL);
        } catch (DataIntegrityViolationException d) {
            d.printStackTrace();
            return Response.error(ResponseStatus.ADUIT_FAIL);
        } catch (Exception e) {
            //????????????
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }

    @Override
    public Response getAllParticipatedMember(Long aid) {

        //1.???redis?????????????????????????????????????????????
        Set<String> userSet = stringRedisTemplate.opsForZSet().range(RedisKeyName.SECOND_KILL_PARTICIPATION + aid, 1, -1);
        List<R_Student> list = new LinkedList<>();

        Object[] userList = userSet.toArray();
        for (Object data : userList){
            //????????????id
            String uid = data.toString().split("-")[0];
            //??????????????????????????????
            String status = data.toString().split("-")[1];
            //2.??????????????????3??????????????????????????????????????????????????????
            if (!status.equals("3")) continue;

            //3.??????uid??????????????????
            User user = redisUtils.queryForValue(RedisKeyName.MANAGE_USER, uid, User.class, 60L, TimeUnit.DAYS, false,
                    (id) -> {
                        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
                        userQueryWrapper.eq(User::getUid, uid);
                        return userMapper.selectOne(userQueryWrapper);
                    });

            //4.??????cid???????????????????????????
            String cIdStr = user.getCid().toString();
            Class classInfo = redisUtils.queryForValue(RedisKeyName.MANAGE_CLASS, cIdStr, Class.class, 60L, TimeUnit.DAYS, true,
                    (id) -> {
                        QueryWrapper<Class> classQueryWrapper = new QueryWrapper<>();
                        classQueryWrapper.eq("cid", cIdStr);
                        return classMapper.selectOne(classQueryWrapper);
                    });

            R_Student student = R_Student.builder().uid(user.getUid())
                    .uname(user.getUname())
                    .phone(user.getPhone())
                    .cname(classInfo.getCname())
                    .grade(classInfo.getGrade())
                    .major(classInfo.getMajor())
                    .college(classInfo.getCollege())
                    .campus(classInfo.getCampus()).build();
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
        // ???????????????
        List<R_Activity_Participation> list = new LinkedList<>();
        for (int i = 0; i < records.size(); i++) {
            Participation participation = (Participation) records.get(i);
            //1.?????????????????????????????????  ??????uid???????????????aid
            //1.1??????uid??????redis??????cid
            Long aid = participation.getAid();
            Activity activity;
            Object activityStr = stringRedisTemplate.opsForHash().get(RedisKeyName.ACTIVITY, aid.toString());
            //1.2 ??????redis???????????????????????????
            if( activityStr != null){
                System.out.println("???redis?????????");
                activity = JSONUtil.toBean(activityStr.toString(), Activity.class);
            }else{

                QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
                activityQueryWrapper.eq("aid",aid);
                System.out.println("?????????????????????");
                activity = activityMapper.selectOne(activityQueryWrapper);
                //??????????????????????????????redis
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
        //1.???????????????????????????Aid?????????????????????
        //1.1 ??????aid????????????
        Long aid = info.getAid();

        Activity activity = redisUtils.queryForValue(RedisKeyName.ACTIVITY, aid.toString(),Activity.class,5L, TimeUnit.MINUTES, true,
                (id) -> {
                    QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("aid", aid);
                    return activityMapper.selectOne(queryWrapper);
                });

        if(activity == null) throw new IllegalArgumentException();

        //1???2 ??????????????????
        Long sid = activity.getAShichangType();

        //2.???????????????????????????

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
