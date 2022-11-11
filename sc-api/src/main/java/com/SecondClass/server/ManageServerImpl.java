package com.SecondClass.server;

import cn.dev33.satoken.stp.StpUtil;
import com.SecondClass.entity.Organization;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import com.SecondClass.entity.User;
import com.SecondClass.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.SecondClass.entity.ResponseStatus.USER_LOGIN_SUCCESS;

/**
 * @ClassName: ManageServerImpl
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/5 19:03
 **/

@Slf4j
@Service
public class ManageServerImpl implements ManageServer {
    @Autowired
    private UserMapper userMapper;

    public Response loginIn(User user) {
        //1.查询是否有该账号
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("uid",user.getUid());
        userQueryWrapper.eq("upassword",user.getUpassword());
        User user1 = userMapper.selectOne(userQueryWrapper);
        if (user1 == null) {
            return Response.error(ResponseStatus.USER_LOGIN_FAIL);
        }
        //2.如果登录成功，授予权限
        StpUtil.login(user1.getUid());
        return Response.success(ResponseStatus.USER_LOGIN_SUCCESS);
    }

    public Response createOrg(Organization request) {
        return null;

    }

    public Response addAccount(User request) {
        return null;

    }


}
