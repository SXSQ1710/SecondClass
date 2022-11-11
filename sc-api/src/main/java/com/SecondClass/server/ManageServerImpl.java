package com.SecondClass.server;

import cn.hutool.core.bean.BeanUtil;
import com.SecondClass.entity.Organization;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import com.SecondClass.entity.User;
import com.SecondClass.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ManageServerImpl
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/5 19:03
 **/

@Slf4j
@Service
public class ManageServerImpl implements ManageServer{

    @Resource
    UserMapper userMapper;

    /**
     * 登录系统
     * @param userMap
     * @return
     */
    public Response loginIn(Map userMap) {
        try{
            //查询登录信息
            List<User> user = userMapper.selectByMap(userMap);
            if(user == null) return Response.success(ResponseStatus.USER_LOGIN_FAIL);
            return Response.success(ResponseStatus.USER_LOGIN_SUCCESS);
        }catch(Exception e){
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }


    public Response createOrg(Organization org) {
        try{

        }catch (DataIntegrityViolationException d){
            //数据库插入失败
            d.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_APPLY_FAIL);
        }catch (Exception e){
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
        return null;

    }

    public Response addAccount(User request) {
        return null;

    }
}
