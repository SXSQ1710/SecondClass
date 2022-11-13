package com.SecondClass.config;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.SecondClass.entity.RedisKeyName;
import com.SecondClass.entity.User;
import com.SecondClass.mapper.UserMapper;
import com.SecondClass.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @title: StpInterfaceImpl
 * @Author SXSQ
 * @Description // 自定义权限验证接口扩展
 * @Date 2022/11/13 15:58
 **/

@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Resource
    RedisUtils redisUtils;
    @Resource
    UserMapper userMapper;
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        Long oid = (Long) StpUtil.getSession().get("o");


        if(loginId.toString().equals("1")){
            // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
            List<String> list = new ArrayList<String>();
            list.add("101");

            return list;
        }else if(loginId.toString().equals("2")){
            List<String> list = new ArrayList<String>();
            list.add("102");
            return list;
        }
        return null;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> list = new ArrayList<String>();
        list.add("admin");
        list.add("super-admin");
        return list;
    }

}
