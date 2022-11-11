package com.SecondClass.server;

import com.SecondClass.entity.*;
import com.SecondClass.entity.Class;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.mapper.ClassMapper;
import com.SecondClass.mapper.UserMapper;
import com.SecondClass.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ManageServerImpl
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/5 19:03
 **/

@Slf4j
@Service
public class ManageServerImpl {

    @Resource
    RedisUtils redisUtils;
    @Resource
    ClassMapper classMapper;
    @Resource
    UserMapper userMapper;

    public Response login(R_SignIn request) {
        return null;
    }

    public Response createOrg(Organization request) {
        return null;

    }

    public Response addAccount(User request) {
        return null;

    }

    public Response getClassById(Long cid) {
        String cIdStr = cid.toString();
        Class classInfo = redisUtils.queryForValue(RedisKeyName.MANAGE_CLASS, cIdStr, Class.class, 60L, TimeUnit.DAYS,
                (id) -> {
                    QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("cid", cIdStr);
                    return classMapper.selectOne(queryWrapper);
                });

        return Response.success(ResponseStatus.MANAGE_CLASS_SUCCESS,classInfo);
    }

    public Response getUserById(Long uid) {
        String uIdStr = uid.toString();
        User userInfo = redisUtils.queryForValue(RedisKeyName.MANAGE_USER, uIdStr, User.class, 60L, TimeUnit.DAYS,
                (id) -> {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("uid", uIdStr);
                    return userMapper.selectOne(queryWrapper);
                });

        return Response.success(ResponseStatus.MANAGE_USER_SUCCESS,userInfo);
    }
}
