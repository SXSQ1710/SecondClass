package com.SecondClass.server;

import com.SecondClass.entity.*;
import com.SecondClass.entity.R_entity.R_Login;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    Response getAllOrg(Page<Organization> page);

    Response getOrg(Organization organization);

    Response changePwd(Map pwdMap);

    Response applyOrg(OrganizationApply orgApply);

    Response getApplyOrg(Page<OrganizationApply> page);

    Response auditOrgApp(Long oAppId, Integer oAppStatus);

    Response getMember(Page<MemberView> page);

    Response getMemberByOid(Integer oid);
}
