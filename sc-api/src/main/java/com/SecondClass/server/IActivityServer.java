package com.SecondClass.server;

import com.SecondClass.entity.Participation;
import com.SecondClass.entity.R_entity.R_ActivityApplication;
import com.SecondClass.entity.R_entity.R_ShiAppInfo;
import com.SecondClass.entity.R_entity.R_SignIn;
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

    Response dynamicGetAllParticipatedMember(Long aid);

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
    Response getAll(Long pageNo, Long pageSize);


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

    /**
     * @param aid 活动id
     * @param uid 用户id
     * @description: TODO 活动管理员申请活动签到码
     * @return: com.SXSQ.bean.Response
     * @author: SXSQ
     * @date: 2022/11/03 13:46
     */
    Response getSignIn(Long aid, Long uid, Integer type);

    Response signInOrOff(R_SignIn signIn);

    /**
     * 查询所有未审核报名人员
     * @param aid
     * @param page
     * @return
     */
    Response getAllRegisteredUser(Long aid, Page page);

    /**
     * 审核报名人员状态
     * @param participation
     * @return
     */
    Response modifyRegisterStatusByUid(Participation participation);

    /**
     *
     * @param aid
     * @return
     */
    Response getAllParticipatedMember(Long aid);

    /**
     * 模糊查询
     * @param aname
     * @return
     */
    Response searchByName(String aname,Page page);

    /**
     * 传入uid查询用户所有活动（所有状态）
     * @param uid
     * @return
     */
    Response getParticipationByUid(String uid,Page page);

    /**
     * 组织提交有效参与人员的信息给校团委。
     * @param info
     * @return
     */
    Response sendInfoToOrganization(R_ShiAppInfo info);
}
