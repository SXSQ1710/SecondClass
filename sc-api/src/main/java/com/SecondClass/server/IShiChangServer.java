package com.SecondClass.server;

import com.SecondClass.entity.Participation;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.Shichang;
import com.SecondClass.entity.ShichangApplication;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

public interface IShiChangServer {

    /**
     * 浏览个人时长
     * @return response
     */
    Response browseMyShiChang();

    /**
     * 审核活动时长
     * @param aid 活动id
     * @param statue 审核状态 4：时长有效，允许发放 5：时长无效，拒绝发放
     * @return response
     */
    Response auditActivityShiChang(Integer aid,Integer statue);

    /**
     * 查看自己参与活动是否发放时长
     * @param uid 用户id
     * @return response
     */
    Response getMyParticipation(Integer uid, Page<Participation> page);

    /**
     * 用于学生查询自己各种类型的总时长
     * @return
     */
    Response queryAllGroupBySid();

    /**
     * 用于学生根据时间查询自己各种类型的总时长
     * @return
     */
    Response queryAllGroupBySidAndTime(Date startTime, Date endTime);

    Response shiChangApplication(ShichangApplication shichangApplication);
}
