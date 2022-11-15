package com.SecondClass.server;

import com.SecondClass.entity.Participation;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.Shichang;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface IShiChangServer {

    /**
     * 浏览个人时长
     * @param uid 用户id
     * @return response
     */
    Response browseMyShiChang(Integer uid);

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

}
