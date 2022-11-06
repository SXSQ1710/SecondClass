package com.SecondClass.server;

import com.SecondClass.entity.Organization;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ManageServerImpl
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/5 19:03
 **/

@Slf4j
@Service
public class ManageServerImpl {

    public Response login(R_SignIn request) {
        return null;
    }

    public Response createOrg(Organization request) {
        return null;

    }

    public Response addAccount(User request) {
        return null;

    }
}
