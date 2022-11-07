package com.SecondClass.server;

import cn.hutool.core.bean.BeanUtil;
import com.SecondClass.entity.Organization;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import com.SecondClass.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName: ManageServerImpl
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/5 19:03
 **/

@Slf4j
@Service
public class ManageServerImpl {

    public Response login(Map userMap) {
        try{
            //获取登录信息
            Long uid =(Long)userMap.get("uid");
            String password =(String)userMap.get("password");
            User user = User.builder().uid(uid).upassword(password).build();
            return Response.success(ResponseStatus.USER_LOGIN_SUCCESS);

        }catch(DataIntegrityViolationException d){
            //数据库插入失败
            d.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_APPLY_FAIL);
        }
    }

    public Response createOrg(Organization request) {
        return null;

    }

    public Response addAccount(User request) {
        return null;

    }
}
