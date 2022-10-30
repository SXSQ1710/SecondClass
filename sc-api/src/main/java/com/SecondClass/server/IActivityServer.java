package com.SecondClass.server;

import com.SecondClass.entity.Participation;
import com.SecondClass.entity.R_entity.R_ActivityApplication;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @title: IActivityServer
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/10/30 13:46
 **/

public interface IActivityServer {
    /**
     * @param request 申请活动信息表接受实体
     * @description: TODO 活动申请
     * @return: com.SXSQ.bean.Response
     * @author: SXSQ
     * @date: 2022/10/30 13:46
     */
    Response applyActivity(R_ActivityApplication request);


    /**
     * 审核活动
     * @param aAppID 申请活动表id
     * @param status 活动状态 2：通过 0：拒绝
     * @return Response
     */
    Response auditActivity(Integer aAppID,Integer status,String explain);

    /**
     * 通过用户id查询申请
     * @param uid 分页
     * @return Response
     */
    Response findActivityAppByUid(Long uid, Page page);

    /**
     *
     * @param aAppid 活动申请id
     * @return Response
     */
    Response findAppStatusByAid(Long aAppid);


    /**
     * 获取所有活动申请
     * @param page 分页
     * @return Response
     */
    Response getAllApp(Page page);

    /**
     * 获取所有活动
     * @param page 分页
     * @return Response
     */
    Response getAll(Page page);


    /**
     * 查询单个活动详细
     * @param aid
     * @return
     */
    Response findActivityByAid(Long aid);

    /**
     * 报名活动
     * @param participation
     * @return
     */
    Response register(Participation participation);

    Response signIn(Participation participation);

    Response signOff(Participation participation);

    Response getAllRegisteredUser(Long aid, Page page);

    Response modifyRegisterStatusByUid(Participation participation);
}
