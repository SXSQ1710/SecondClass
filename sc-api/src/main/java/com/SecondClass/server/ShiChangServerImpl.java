package com.SecondClass.server;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.SecondClass.entity.*;
import com.SecondClass.entity.Class;
import com.SecondClass.entity.R_entity.R_ShiChang;
import com.SecondClass.mapper.*;
import com.SecondClass.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        String[] split;
        for (String value : range){
            split = value.split("-");
            System.out.println(Arrays.toString(split));
        }
//        range.forEach(value-> {
//            String[] split = value.split("-");
//
//            System.out.println(Arrays.toString(split));
//        });

        return Response.success(ResponseStatus.SUCCESS,semester);
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
    public Response shiChangApplication(ShichangApplication shichangApplication) {
        String user_id = StpUtil.getLoginId().toString();
        shichangApplication.setUid(Long.parseLong(user_id));
        System.out.println(shichangApplication);
        return null;
    }
}
