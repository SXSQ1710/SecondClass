package com.SecondClass.server;

import com.SecondClass.entity.*;
import com.SecondClass.mapper.ActivityMapper;
import com.SecondClass.mapper.ParticipationMapper;
import com.SecondClass.mapper.ShiChangMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ShiChangServerImpl implements IShiChangServer {

    @Resource
    private ShiChangMapper shiChangMapper;

    @Resource
    private ParticipationMapper participationMapper;

    @Resource
    private ActivityMapper activityMapper;

    @Override
    public Response browseMyShiChang(Integer uid) {
        LambdaQueryWrapper<Shichang> queryWrapper = Wrappers.<Shichang>lambdaQuery()
                .eq(Shichang::getUid,uid);
        List<Shichang> shichangs = shiChangMapper.selectList(queryWrapper);
        return Response.success(ResponseStatus.SUCCESS,shichangs);
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
                shichang.setSid(activity.getAShichangType());
                shichang.setSnum(activity.getAShichangNum());
                shichang.setAcquireTime(new Date());
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

}
