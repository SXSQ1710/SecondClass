package com.SecondClass.server;

import com.SecondClass.entity.Organization;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @ClassName: ManageServer
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/5 19:04
 **/
public interface ManageServer {

    Response loginIn(User user);

    Response createOrg(Organization request);

    Response addAccount(User request);


}
