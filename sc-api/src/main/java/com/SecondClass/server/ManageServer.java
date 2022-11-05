package com.SecondClass.server;

import com.SecondClass.entity.Oganization;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.User;

/**
 * @ClassName: ManageServer
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/5 19:04
 **/
public interface ManageServer {

    Response login(R_SignIn request);

    Response createOrg(Oganization request);

    Response addAccount(User request);


}
