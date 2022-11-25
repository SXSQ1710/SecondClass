package com.SecondClass.server;

import com.SecondClass.entity.Organization;
import com.SecondClass.entity.OrganizationApply;
import com.SecondClass.entity.R_entity.R_Login;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
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
public interface IManageServer extends IService<User>{

    Response login(R_Login rLogin);

    Response createOrg(Organization organization );

    Response addAccount(User user);

    Response addAccountByBatch(MultipartFile file);

    Response getAllOrg(int pageNo);

    Response getOrg(Map orgMap);

    Response changePwd(Map pwdMap);

    Response applyOrg(OrganizationApply orgApply);

    Response getApplyOrg(int pageNo);

    Response auditOrgApp(Long oAppId, Integer oAppStatus);

    Response getMember();
}
