package com.SecondClass.server;

import com.SecondClass.entity.Organization;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @ClassName: ManageServer
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/5 19:04
 **/
public interface ManageServer extends IService<User>{

    Response login(Map userMap);

    Response createOrg(Organization organization );

    Response addAccount(User user);

//    Response addAccountByBatch(List<User> userList);

    Response getAllOrg(int pageNo);

    Response changePwd(Map pwdMap);
}
