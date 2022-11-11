package com.SecondClass.server;

import com.SecondClass.entity.Organization;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @ClassName: ManageServer
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/5 19:04
 **/
public interface ManageServer {

    /**
     * 登录系统
     * @param userMap
     * @return
     */
    Response login(Map userMap);

    /**
     * 创建组织
     * @param request
     * @return
     */
    Response createOrg(Organization request);

    /**
     * 导入账号
     * @param request
     * @return
     */
    Response addAccount(User request);


}
