package com.SecondClass.server;

import com.SecondClass.entity.Activity;
import com.SecondClass.entity.R_entity.R_ActicityApplication;
import com.SecondClass.entity.Response;

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
    Response applyActivity(R_ActicityApplication request);


}
