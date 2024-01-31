package com.SecondClass.scheduled;

import com.SecondClass.mapper.ParticipationMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.Calendar;

/**
 * @title: DataBaseClear
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/10/3 20:10
 **/

@Component
public class DataBaseClear {
    @Resource
    ParticipationMapper participationMapper;

    /**
     * 每年8月1日执行对于活动参与表的数据迁移
     */
    @Scheduled(cron ="0 0 0 1 8 ?")
    @Transactional
    public void dataBaseClear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - 4;
        participationMapper.creatParticipationTemp((long) year);
        dataBaseClear((long) year);
    }

    @Transactional
    public void dataBaseClear(Long grade){
        participationMapper.creatParticipationTemp( grade);
    }
}
