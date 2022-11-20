package com.SecondClass.server;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.SecondClass.entity.*;
import com.SecondClass.entity.Class;
import com.SecondClass.entity.R_entity.R_ShiChang;
import com.SecondClass.mapper.ActivityMapper;
import com.SecondClass.mapper.ParticipationMapper;
import com.SecondClass.mapper.ShiChangMapper;
import com.SecondClass.mapper.ShiChangTypeMapper;
import com.SecondClass.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ShiChangServerImpl implements IShiChangServer {

    @Resource
    private ShiChangMapper shiChangMapper;

    @Resource
    ShiChangTypeMapper shichangTypeMapper;

    @Resource
    private ParticipationMapper participationMapper;

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    RedisUtils redisUtils;

    @Override
    public Response browseMyShiChang(Integer uid) {

        LambdaQueryWrapper<Shichang> queryWrapper = Wrappers.<Shichang>lambdaQuery()
                .eq(Shichang::getUid,uid);
        Shichang shichang = shiChangMapper.selectOne(queryWrapper);

        //[[1 ,[2 ,["1-2"],["1-2"]],[2 ,["1-2"],["1-2"]],[2 ,["1-2"],["1-2"]]]]
        JSONArray objects = JSONUtil.parseArray(shichang.getShiChang());
        List<List> info = JSONUtil.toList(shichang.getShiChang(), List.class);
        HashMap<Object, Object> shiChangInfo = new HashMap<>();
        for (List data :info){
            System.out.println(data.get(0));
            String shiChangTypeId = String.valueOf(data.get(0));
//            redisUtils.queryForValue(RedisKeyName.SHICHANG_TYPE, shiChangTypeId, ShichangType.class, 60L, TimeUnit.DAYS, true,
//                (id) -> {
//                    QueryWrapper<Class> shiChangTypeQueryWrapper = new QueryWrapper<>();
//                    shiChangTypeQueryWrapper.eq("sid", shiChangTypeId);
//                    return .selectOne(shiChangTypeQueryWrapper);
//                });


//            List<List> lists = JSONUtil.toList((JSONArray) data, List.class);
//            System.out.println(lists.get(1).get(1));
//            JSONUtil.toList(data, Object.class);
//
//            redisUtils.queryForValue(RedisKeyName.SHICHANG_TYPE, cIdStr, Class.class, 60L, TimeUnit.DAYS, true,
//                    (id) -> {
//                        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
//                        queryWrapper.eq("cid", cIdStr);
//                        return classMapper.selectOne(queryWrapper);
//                    });
//            shiChangInfo.put("")
        }
        return Response.success(ResponseStatus.SUCCESS,objects);
    }

    @Override
    @Transactional
    public Response auditActivityShiChang(Integer aid, Integer statue) {
        LambdaQueryWrapper<Participation> queryWrapper = Wrappers.<Participation>lambdaQuery()
                .eq(Participation::getAid,aid);
        List<Participation> participations = participationMapper.selectList(queryWrapper);
        //统计当前活动时长审核次数
        int count = 0;
        if (statue==4) {
            //发放时长
            for (Participation participation : participations) {
                participation.setParticipateStatus(4);
                LambdaQueryWrapper<Activity> wrapper = Wrappers.<Activity>lambdaQuery()
                        .eq(Activity::getAid,aid);
                Activity activity = activityMapper.selectOne(wrapper);
                Shichang shichang = new Shichang();
                shichang.setUid(participation.getUid());
//                shichang.setSid(activity.getAShichangType());
//                shichang.setSnum(activity.getAShichangNum());
//                shichang.setAcquireTime(new Date());
                int insert = shiChangMapper.insert(shichang);
                //插入成功时才能修改参加活动表中的状态
                if (insert>0) {
                    int update = participationMapper.updateById(participation);
                    if (update>0) count++;
                }
            }
        }else {
            //拒绝发放时长
            for (Participation participation : participations) {
                participation.setParticipateStatus(5);
                int update = participationMapper.updateById(participation);
                if (update>0) count++;
            }
        }
        return count==participations.size() ? Response.success(ResponseStatus.SUCCESS):Response.error(ResponseStatus.ERROR);
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
}
