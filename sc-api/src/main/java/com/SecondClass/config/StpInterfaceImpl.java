package com.SecondClass.config;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.SecondClass.entity.Organization;
import com.SecondClass.entity.RedisKeyName;
import com.SecondClass.entity.User;
import com.SecondClass.mapper.OrganizationMapper;
import com.SecondClass.mapper.UserMapper;
import com.SecondClass.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Resource
    OrganizationMapper organizationMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        String oidListStr = (String) StpUtil.getSession().get("o");
        List<Integer> oidList = JSONUtil.toList(oidListStr, Integer.class);
        int level = 999;
        for (int i = 0; i < oidList.size(); i++) {
            String oid = String.valueOf(oidList.get(i));
            //1.先查询redis缓存
            int permission;
            String json = stringRedisTemplate.opsForValue().get(RedisKeyName.MANAGE_ORGANIZATION_LEVEL + oid);
            boolean notBlank = StrUtil.isNotBlank(json);
            if (notBlank){
                //1.1.redis缓存命中，返回结果
                permission = Integer.parseInt(json);
                level = (level > permission ? permission:level);
                continue;
            }

            //2.redis缓存未命中，查询数据库，查询数据库的代码通过函数传入
            LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Organization::getOid, oid);
            permission = organizationMapper.selectOne(queryWrapper).getPermissionsLevel();
            redisUtils.setValue(RedisKeyName.MANAGE_ORGANIZATION_LEVEL + oid, permission+"");
            if (permission < level) level = permission;
        }

        //角色划分：admin(校团委)、manage(普通组织)、user(学生)
        if(level == 1){
            // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
            List<String> list = new ArrayList<String>();
            list.add("user");
            list.add("manage");
            list.add("admin");
            return list;
        }else if(level == 2){
            List<String> list = new ArrayList<String>();
            list.add("user");
            list.add("manage");
            return list;
        }else {
            List<String> list = new ArrayList<String>();
            list.add("user");
            return list;
        }

    }

}
