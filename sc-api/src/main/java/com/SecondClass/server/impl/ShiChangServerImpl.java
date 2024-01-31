package com.SecondClass.server.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.SecondClass.entity.*;
import com.SecondClass.entity.R_entity.R_ShiChang;
import com.SecondClass.mapper.*;
import com.SecondClass.server.IShiChangServer;
import com.SecondClass.utils.DateUtils;
import com.SecondClass.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ShiChangServerImpl implements IShiChangServer {

    @Resource
    private ShiChangMapper shiChangMapper;

    @Resource
    private ShiChangTypeMapper shichangTypeMapper;

    @Resource
    private SelfApplicationMapper selfApplicationMapper;

    @Resource
    private ParticipationMapper participationMapper;

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private SemesterMapper semesterMapper;

    @Resource
    private ShichangApplicationMapper shichangApplicationMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RedisUtils redisUtils;

    @Override
    @Transactional
    public Response browseMyShiChang() {

        //redis查询时长信息，被动更新
        String user_id = StpUtil.getLoginId().toString();
        Shichang shichang = redisUtils.queryForValue(RedisKeyName.SHICHANG_INFO, user_id, Shichang.class, 60L, TimeUnit.DAYS, true,
                (id)->{
                    LambdaQueryWrapper<Shichang> queryWrapper = Wrappers.<Shichang>lambdaQuery()
                            .eq(Shichang::getUid,user_id);
                    return shiChangMapper.selectOne(queryWrapper);
                });

        if (!BeanUtil.isNotEmpty(shichang)) {
                List<ShichangType> shichangTypes = shichangTypeMapper.selectList(null);

                String shiChangInfo = "[";
                int size = shichangTypes.size();
                for (int i = 0; i < size - 1; i++) {
                    shiChangInfo += "[" + shichangTypes.get(i).getSid().toString() + " ,[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]],";
                }
                shiChangInfo +=  "[" + shichangTypes.get(size-1).getSid().toString() + " ,[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]]]";

                shichang = Shichang.builder().uid(Long.valueOf(user_id)).shiChang(shiChangInfo).build();
                shiChangMapper.insert(shichang);
                redisUtils.setValue(RedisKeyName.SHICHANG_INFO+user_id, shichang,  60L, TimeUnit.DAYS);
        }

//        数据库个人时长储存格式：
//         [时长类型id,大一上[时长总数,参与活动id列表,自主申报id列表],大一下[时长总数,参与活动id列表,自主申报id列表],大二上,大二下,大三上,大三下,]
//        [[1 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]],
//         [2 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]],
//         [3 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]],
//         [4 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]]]

        List<List> allInfo = JSONUtil.toList(shichang.getShiChang(), List.class);
        List<Map<Object, Object>> R = new ArrayList<>();
        for (List data :allInfo){
            //获取时长类型
            String shiChangTypeId = String.valueOf(data.get(0));

            ShichangType shichangType = redisUtils.queryForValue(RedisKeyName.SHICHANG_TYPE, shiChangTypeId, ShichangType.class, 60L, TimeUnit.DAYS, true,
                    (id) -> {
                        LambdaQueryWrapper<ShichangType> shiChangTypeQueryWrapper = new LambdaQueryWrapper<>();
                        shiChangTypeQueryWrapper.eq(ShichangType::getSid, shiChangTypeId);
                        return shichangTypeMapper.selectOne(shiChangTypeQueryWrapper);
                    });
            Map<Object, Object> shiChangInfo = new HashMap<>();
            HashMap<Object, Object> oneShiChang = new HashMap<>();

            shiChangInfo.put("name",shichangType.getShichangName());

            for (int i = 1; i < data.size(); i++) {
                //学期标号，大一上为 1-1，大一下为 1-2，大三下为 3-2
                String xueQi = (i%2 + i/2)+ "-" +(i%2 == 0 ? 2:1);
                List<Object> list = JSONUtil.toList((JSONArray) data.get(i), Object.class);
                //时长信息总和
                HashMap<Object, Object> hashMap1 = new HashMap<>();
                //活动时长信息
                HashMap<Object, Object> hashMap2 = new HashMap<>();
                //自主申报信息
                HashMap<Object, Object> hashMap3 = new HashMap<>();
                //时长总和
                hashMap1.put("total",list.get(0));

                String[] activities = list.get(1).toString().split("-");
                String[] selfApps = list.get(2).toString().split("-");

                if (activities.length != 0){
                    //活动时长来源
                    for(String aid : activities){
                        Activity activity = redisUtils.queryForValue(RedisKeyName.ACTIVITY, aid, Activity.class, 10L, TimeUnit.DAYS, true,
                                (id) -> {
                                    QueryWrapper<Activity> aQueryWrapper = new QueryWrapper<>();
                                    aQueryWrapper.eq("aid", aid);
                                    return activityMapper.selectOne(aQueryWrapper);
                                });
                        hashMap2.put(activity.getAname(),activity.getAShichangNum());
                    }
                }
                if (selfApps.length != 0){
                    //自主申报时长来源
                    for(String said : selfApps){
                        SelfApplication selfApplication = redisUtils.queryForValue(RedisKeyName.SELF_APPLICATION, said, SelfApplication.class, 10L, TimeUnit.DAYS, true,
                                (id) -> {
                                    LambdaQueryWrapper<SelfApplication> sQueryWrapper = new LambdaQueryWrapper<>();
                                    sQueryWrapper.eq(SelfApplication::getSelfAppId, said);
                                    return selfApplicationMapper.selectOne(sQueryWrapper);
                                });
                        hashMap3.put(selfApplication.getSelfAppDescription(),selfApplication.getSelfAppShiNum());
                    }
                }

                hashMap1.put("activity",hashMap2);
                hashMap1.put("selfApp",hashMap3);
                oneShiChang.put(xueQi,hashMap1);
            }

            shiChangInfo.put("value",oneShiChang);

            R.add(shiChangInfo);
        }
        return Response.success(ResponseStatus.SUCCESS,R);
    }

    @Override
    @Transactional
    public Response auditActivityShiChang(Integer aid, Integer statue) {

        Semester semester = redisUtils.queryForValue(RedisKeyName.SHICHANG_SEMESTER, "1", Semester.class, 10L, TimeUnit.DAYS, true,
                (id) -> {
                    return semesterMapper.selectById(1);
                });

        Set<String> range = stringRedisTemplate.opsForZSet().range(RedisKeyName.SECOND_KILL_PARTICIPATION + aid, 1, -1);

        //异步任务开始
        Activity activity = redisUtils.queryForValue(RedisKeyName.ACTIVITY, aid+"", Activity.class, 10L, TimeUnit.DAYS, true,
                (id) -> {
                    QueryWrapper<Activity> aQueryWrapper = new QueryWrapper<>();
                    aQueryWrapper.eq("aid", aid);
                    return activityMapper.selectOne(aQueryWrapper);
                });

        Long aShiChangType = activity.getAShichangType(); //时长类型id
        Integer aShiChangNum = activity.getAShichangNum(); //时长数量
        int aSemester; //活动进行时的学期

        if(DateUtils.isEffectiveDate(activity.getAHoldStart(), semester.getLastSemesterBegin(), semester.getLastSemesterEnd())){
            aSemester = 1;
        }else if (DateUtils.isEffectiveDate(activity.getAHoldStart(), semester.getNextSemesterBegin(), semester.getNextSemesterEnd())){
            aSemester = 2;
        }else {
            return Response.error(ResponseStatus.ERROR);
        }
        //System.out.println("aSemester:"+aSemester);

//        数据库个人时长储存格式：
//         [时长类型id,大一上[时长总数,参与活动id列表,自主申报id列表],大一下[时长总数,参与活动id列表,自主申报id列表],大二上,大二下,大三上,大三下,]
//        [[1 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]],
//         [2 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]],
//         [3 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]],
//         [4 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]]]

        String[] split;
        for (String value : range){
            split = value.split("-");
            //System.out.println(Arrays.toString(split));
            String uid = split[0]; //用户id
            String status = split[1]; //活动参与状态码
            if (status.equals("3")){

                User user = redisUtils.queryForValue(RedisKeyName.MANAGE_USER, uid, User.class, 60L, TimeUnit.DAYS, false,
                        (id) -> {
                            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                            queryWrapper.eq(User::getUid, uid);
                            return userMapper.selectOne(queryWrapper);
                        });

                Calendar cal = Calendar.getInstance();
                int nowYear = cal.get(Calendar.YEAR);
                ///需修改
                int grade = nowYear - user.getGrade() ;
                System.out.println("grade:"+ grade );

                LambdaQueryWrapper<Shichang> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Shichang::getUid, uid);
                Shichang shichang = shiChangMapper.selectOne(wrapper);

                //用户时长记录未创建
                if (!BeanUtil.isNotEmpty(shichang)){
                    List<ShichangType> shichangTypes = shichangTypeMapper.selectList(null);

                    String shiChangInfo = "[";
                    int size = shichangTypes.size();
                    ArrayList<String> strings = new ArrayList<>();

                    for (int i = 0; i < size; i++) {
                        Long shiChangTypeId = shichangTypes.get(i).getSid();
                        String r;

                        if(shiChangTypeId.equals(aShiChangType)){
                            ArrayList<String> aShiChang = new ArrayList<>();
                            aShiChang.add(shiChangTypeId.toString());
                            for (int j = 1; j < 7; j++) {
                                if ((aSemester + (grade - 1)*2) == j){
                                    aShiChang.add("["+ aShiChangNum.toString() +",\"" + aid.toString() + "\",\"-\"]");
                                }else{
                                    aShiChang.add("[0 ,\"-\",\"-\"]");
                                }
                            }
                             r = "["+ StringUtils.collectionToDelimitedString(aShiChang, ",")+"]";
                        }else {
                            r = "[" + shiChangTypeId.toString() + ",[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]]";
                        }
                        strings.add(r);
                    }
                    shiChangInfo += StringUtils.collectionToDelimitedString(strings,",") + "]";
//                    System.out.println(shiChangInfo);

                    shichang = Shichang.builder().uid(Long.valueOf(uid)).shiChang(shiChangInfo).build();
                    shiChangMapper.insert(shichang);
                    redisUtils.setValue(RedisKeyName.SHICHANG_INFO + uid, shichang,  60L, TimeUnit.DAYS);

                }else {

                    //数据库个人时长储存格式：
                    //[时长类型id,大一上[时长总数,参与活动id列表,自主申报id列表],大一下[时长总数,参与活动id列表,自主申报id列表],大二上,大二下,大三上,大三下,]
                    //[[1 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]],
                    // [2 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]],
                    // [3 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]],
                    // [4 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]]]

                    List<List> allInfo = JSONUtil.toList(shichang.getShiChang(), List.class);

                    //data : [1 ,[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"],[2 ,"1-2","1"]]
                    List data;
                    for (int i = 0; i < allInfo.size(); i++) {
                        data = allInfo.get(i);
                        if ((int)data.get(0) == aShiChangType.intValue()){

                            for (int j = 1; j < data.size(); j++) {
                                System.out.println((aSemester + (grade - 1) * 2));
                                if ((aSemester + (grade - 1) * 2) == j) {
                                    //data.get(i) : [2 ,"1-2","1"]
                                    List<Object> list = JSONUtil.toList((JSONArray) data.get(j), Object.class);

                                    String[] activities = list.get(1).toString().split("-");
                                    ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(activities));
                                    //System.out.println(arrayList.toString());
                                    arrayList.add(aid.toString());
                                    list.set(1, StringUtils.collectionToDelimitedString(arrayList, "-"));
                                    list.set(0, (int)list.get(0) + aShiChangNum);
                                    String jsonStr = JSONUtil.toJsonStr(list);
                                    data.set(j, jsonStr);
                                    allInfo.set(i, data);
                                }
                            }
                        }else {
                            continue;
                        }
                    }

                    shichang.setShiChang(allInfo.toString());
                    shiChangMapper.update(shichang, wrapper);
                    stringRedisTemplate.delete(RedisKeyName.SHICHANG_INFO + uid);
                }




            }else {
                continue;
            }

        }

        return Response.success(ResponseStatus.SUCCESS,semester);


//        range.forEach(value-> {
//            String[] split = value.split("-");
//
//            System.out.println(Arrays.toString(split));
//        });

//        LambdaQueryWrapper<Participation> queryWrapper = Wrappers.<Participation>lambdaQuery()
//                .eq(Participation::getAid,aid);
//        List<Participation> participations = participationMapper.selectList(queryWrapper);
//        //统计当前活动时长审核次数
//        int count = 0;
//        if (statue==4) {
//            //发放时长
//            for (Participation participation : participations) {
//                participation.setParticipateStatus(4);
//                LambdaQueryWrapper<Activity> wrapper = Wrappers.<Activity>lambdaQuery()
//                        .eq(Activity::getAid,aid);
//                Activity activity = activityMapper.selectOne(wrapper);
//                Shichang shichang = new Shichang();
//                shichang.setUid(participation.getUid());
////                shichang.setSid(activity.getAShichangType());
////                shichang.setSnum(activity.getAShichangNum());
////                shichang.setAcquireTime(new Date());
//                int insert = shiChangMapper.insert(shichang);
//                //插入成功时才能修改参加活动表中的状态
//                if (insert>0) {
//                    int update = participationMapper.updateById(participation);
//                    if (update>0) count++;
//                }
//            }
//        }else {
//            //拒绝发放时长
//            for (Participation participation : participations) {
//                participation.setParticipateStatus(5);
//                int update = participationMapper.updateById(participation);
//                if (update>0) count++;
//            }
//        }
//        return count==participations.size() ? Response.success(ResponseStatus.SUCCESS):Response.error(ResponseStatus.ERROR);
    }

    @Override
    public Response getMyParticipation(Integer uid, Page<Participation> page) {
        LambdaQueryWrapper<Participation> queryWrapper = Wrappers.<Participation>lambdaQuery()
                .eq(Participation::getUid,uid);
        Page<Participation> ret = participationMapper.selectPage(page, queryWrapper);
        return Response.success(ResponseStatus.SUCCESS,ret);
    }

    @Override
    public Response queryAllGroupBySid() {
        Long uid = Long.valueOf((String) StpUtil.getLoginId());
        List<R_ShiChang>  list = shiChangMapper.queryAllGroupBySid(uid);
        return Response.success(ResponseStatus.SUCCESS,list);
    }

    @Override
    public Response queryAllGroupBySidAndTime(Date startTime, Date endTime) {
        Long uid = Long.valueOf((String) StpUtil.getLoginId());
        List<R_ShiChang>  list = shiChangMapper.queryAllGroupBySidAndTime(uid,startTime,endTime);
        return Response.success(ResponseStatus.SUCCESS,list);
    }

    @Override
    @Transactional
    public Response postShiChangApplication(ShichangApplication shichangApplication) {
        try{
            String user_id = StpUtil.getLoginId().toString();
            shichangApplication.setUid(Long.parseLong(user_id));
            shichangApplication.setShiAppStatus(1);
            if (shichangApplicationMapper.insert(shichangApplication) == 1){
                return Response.success(ResponseStatus.SHICHANG_APP_SUCCESS);
            }else {
                return Response.error(ResponseStatus.SHICHANG_APP_FAIL);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(ResponseStatus.SHICHANG_APP_ERROR);
        }
    }

    @Override
    public Response getShiChangApplication(Page<ShichangApplication> page) {
        String user_id = StpUtil.getLoginId().toString();
        LambdaQueryWrapper<ShichangApplication> queryWrapper = Wrappers.<ShichangApplication>lambdaQuery()
                .eq(ShichangApplication::getUid,user_id);
        Page<ShichangApplication> ret = shichangApplicationMapper.selectPage(page, queryWrapper);
        return Response.success(ResponseStatus.SUCCESS,ret);
    }

    @Override
    public Response getAllShiChangApplication(Page<ShichangApplication> page) {
        LambdaQueryWrapper<ShichangApplication> queryWrapper = Wrappers.lambdaQuery();
        Page<ShichangApplication> ret = shichangApplicationMapper.selectPage(page, queryWrapper);
        return Response.success(ResponseStatus.SUCCESS,ret);
    }

    @Override
    public Response getShiAppInfo(Integer sAppId) {

        ShichangApplication shichangApplication = redisUtils.queryForValue(RedisKeyName.SHICHANG_APPLICATION, sAppId + "", ShichangApplication.class, 10L, TimeUnit.DAYS, true,
                (id) -> {
                    return shichangApplicationMapper.selectById(sAppId);
                });

        if (!BeanUtil.isNotEmpty(shichangApplication)) return Response.error(ResponseStatus.ERROR);

        String aid = shichangApplication.getAid().toString();

        Activity activity = redisUtils.queryForValue(RedisKeyName.ACTIVITY, aid, Activity.class, 10L, TimeUnit.DAYS, true,
                (id) -> {
                    return activityMapper.selectById(aid);
                });

        Map<String,Object> R = new HashMap<>();

        R.put("uid",shichangApplication.getUid());
        R.put("shiAppDescription",shichangApplication.getShiAppDescription());
        R.put("shiAppStatus",shichangApplication.getShiAppStatus());
        R.put("activityInfo",activity);

        return Response.success(ResponseStatus.SUCCESS,R);
    }
}
