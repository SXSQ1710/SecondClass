package com.SecondClass.server;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.SecondClass.entity.*;
import com.SecondClass.entity.Class;
import com.SecondClass.mapper.ClassMapper;
import com.SecondClass.mapper.OrganizationMapper;
import com.SecondClass.mapper.UserMapper;
import com.SecondClass.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ManageServerImpl
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/5 19:03
 **/

@Slf4j
@Service
@Component
public class ManageServerImpl extends ServiceImpl<UserMapper,User> implements ManageServer{

    @Resource
    RedisUtils redisUtils;
    @Resource
    ClassMapper classMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    OrganizationMapper organizationMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;
//    @Resource
//    ManageServer manageserver;



    /**
     * @Author jiang
     * @Description //登录
     * @Date 15:18 2022/11/12
     * @Param [userMap]
     * @return com.SecondClass.entity.Response
     **/
    public Response login(Map userMap) {
        try {
            //1.查询登录信息
            String uid = userMap.get("uid").toString();
            User user = redisUtils.queryForValue(RedisKeyName.MANAGE_USER, uid, User.class, 60L, TimeUnit.DAYS, false,
                    (id) -> {
                        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(User::getUid, userMap.get("uid"))
                                .eq(User::getUpassword, userMap.get("upassword"));
                        return userMapper.selectOne(queryWrapper);
                    });

            //1.1查询失败返回错误信息
            if (user == null || !user.getUpassword().equals(userMap.get("upassword").toString())) return Response.success(ResponseStatus.USER_LOGIN_FAIL);
            //2.登录成功
            //2.1 saToke登记用户信息
            StpUtil.login(uid);
            //2.2 saToken保存用户组织
            StpUtil.getSession(true).set("o", user.getOid());

            return Response.success(ResponseStatus.USER_LOGIN_SUCCESS);
        } catch (Exception e) {
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }


    /**
     * @Author jiang
     * @Description //创建组织账号
     * @Date 15:19 2022/11/12
     * @Param [organization]
     * @return com.SecondClass.entity.Response
     **/
    @Transactional
    public Response createOrg(Organization organization) {
        try {
            //查询负责人是否存在
            String uid = organization.getUid().toString();
            User user = redisUtils.queryForValue(RedisKeyName.MANAGE_USER, uid, User.class, 60L, TimeUnit.DAYS, false,
                    (id) -> {
                        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(User::getUid, uid);
                        return userMapper.selectOne(queryWrapper);
                    });
            //设置权限为2(普通组织)
            organization.setPermissionsLevel(2);

            //数据库插入组织信息
            int i = 0;
            if(user != null) i = organizationMapper.insert(organization);
            else return Response.success(ResponseStatus.CREATE_ORGANIZATION_FAIL);
            //更新user表中负责人的所属组织
            int i1 = 0;
            if(i == 1){
                //更新新的组织负责人所属组织字段
                System.out.println(organization.getOid());
                String jsonStr = user.getOid();
                List<Integer> oidList = JSONUtil.toList(jsonStr, Integer.class);
                oidList.add(organization.getOid().intValue());
                user.setOid(JSONUtil.toJsonStr(oidList));
                i1 = userMapper.updateById(user);
                //删除redis相应的用户缓存
                stringRedisTemplate.delete(RedisKeyName.MANAGE_USER + uid);
            }else return Response.success(ResponseStatus.CREATE_ORGANIZATION_FAIL);

            if(i1 == 1) return Response.success(ResponseStatus.CREATE_ORGANIZATION_SUCCESS);
            else return Response.success(ResponseStatus.CREATE_ORGANIZATION_FAIL);
        } catch (DataIntegrityViolationException d){
            //数据库插入失败
            d.printStackTrace();
            return Response.error(ResponseStatus.CREATE_ORGANIZATION_FAIL);
        }catch (Exception e) {
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }

    /**
     * @Author jiang
     * @Description //导入单个学生账号
     * @Date 15:19 2022/11/12
     * @Param [user]
     * @return com.SecondClass.entity.Response
     **/
    public Response addAccount(User user) {
        try {
            //检查数据库是否已经存在账号
            User uCheck = userMapper.selectById(user.getUid());
            if(uCheck!=null){
                System.out.println("存在");
                return Response.success(ResponseStatus.CREATE_USER_FAIL );
            }
            //导入账号
            int i = 0;
            i = userMapper.insert(user);
            if(i==1)return Response.success(ResponseStatus.CREATE_USER_SUCCESS);
            else return Response.success(ResponseStatus.CREATE_USER_FAIL );
        } catch (DataIntegrityViolationException d){
            //数据库插入失败
            d.printStackTrace();
            return Response.error(ResponseStatus.CREATE_USER_FAIL);
        }catch (Exception e) {
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }

    }
    /**
     * @Author jiang
     * @Description //获取所有的组织
     * @Date 18:18 2022/11/12
     * @Param [pageNo]
     * @return com.SecondClass.entity.Response
     **/
    @Override
    public Response getAllOrg(int pageNo) {
        Page<Organization> page = new Page<>(pageNo,15);
        IPage<Organization> orgPage = organizationMapper.selectPage(page,null);
        List<Organization> organizationList = orgPage.getRecords();
        if(organizationList.size()==0)return Response.success(ResponseStatus.ORGANIZATION_QUERY_FAIL);
        return Response.success(ResponseStatus.ORGANIZATION_QUERY_SUCCESS,organizationList);
    }

    /**
     * @Author jiang
     * @Description //根据提供的信息查询组织
     * @Date 22:10 2022/11/12
     * @Param [orgMap]
     * @return com.SecondClass.entity.Response
     **/
    @Override
    public Response getOrg(Map orgMap) {
        try {
            LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.or((item)->{
                item.like(Organization::getOid,orgMap.get("oid"))
                    .like(Organization::getOname,orgMap.get("oname"))
                    .like(Organization::getSuperiorOrganization,orgMap.get("superiorOrganization"))
                    .like(Organization::getOdescription,orgMap.get("odescription"))
                    .like(Organization::getUid,orgMap.get("uid"))
                    .like(Organization::getCampus,orgMap.get("campus"));});
            List<Organization> organizationList = organizationMapper.selectList(queryWrapper);
            if (organizationList.size()==0) return Response.success(ResponseStatus.ORGANIZATION_QUERY_FAIL);
            return Response.success(ResponseStatus.ORGANIZATION_QUERY_SUCCESS,organizationList);
        } catch (Exception e) {
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }

    /**
     *
     * @param oid
     * @return
     */
    public Response getOrgById(Long oid){
        Organization organization = redisUtils.queryForValue(RedisKeyName.MANAGE_ORGANIZATION, oid.toString(), Organization.class, 60L, TimeUnit.DAYS, true,
                (id) -> {
                    LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Organization::getOid, oid.toString());
                    return organizationMapper.selectOne(queryWrapper);
                });

        return Response.success(ResponseStatus.ORGANIZATION_QUERY_SUCCESS, organization);
    }

    /**
     * @Author jiang
     * @Description //修改密码
     * @Date 22:35 2022/11/12
     * @Param [pwdMap]
     * @return com.SecondClass.entity.Response
     **/
    @Override
    public Response changePwd(Map pwdMap) {
        try {
            UpdateWrapper<User > updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("upassword",pwdMap.get("newpwd"))
                    .eq("upassword",pwdMap.get("oldpwd")).
                    eq("uid",pwdMap.get("uid"));
            int i =0;
            i = userMapper.update(null,updateWrapper);
            if (i == 0) return Response.success(ResponseStatus.CHANGE_USERPWD_FAIL);
            return Response.success(ResponseStatus.CHANGE_USERPWD_SUCCESS);
        } catch (DataIntegrityViolationException d){
            //数据库插入失败
            d.printStackTrace();
            return Response.error(ResponseStatus.CHANGE_USERPWD_FAIL);
        }catch (Exception e) {
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }

    @Override
    public Response applyOrg(OrganizationApply orgApply) {
        return null;
    }


    /**
     * @Author jiang
     * @Description //批量导入账号，报错中，没调好
     * @Date 17:53 2022/11/12
     * @Param [userList]
     * @return com.SecondClass.entity.Response
     **/
    /**
    public Response addAccountByBatch(List<User> userList) {
        try {
            //检查是否已存在用户
            for(User user:userList ){
                User uCheck = userMapper.selectById(user.getUid());
                if(uCheck!=null){
                    System.out.println("存在");
                    return Response.success(ResponseStatus.CREATE_USER_FAIL );
                }
            }
            User user1 = new User();
            user1.setUname("lala");
            boolean save = manageserver.save(user1);
            System.out.println(save);
            System.out.println(userList);
            boolean b = manageserver.saveBatch(userList);
            if (b) return Response.success(ResponseStatus.CREATE_USER_SUCCESS);
            else return Response.success(ResponseStatus.CREATE_USER_FAIL);
        } catch (DataIntegrityViolationException d){
            //数据库插入失败
            d.printStackTrace();
            return Response.error(ResponseStatus.CREATE_ORGANIZATION_FAIL);
        }catch (Exception e) {
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }

    }
     **/

    public Response getClassById(Long cid) {
        String cIdStr = cid.toString();
        Class classInfo = redisUtils.queryForValue(RedisKeyName.MANAGE_CLASS, cIdStr, Class.class, 60L, TimeUnit.DAYS, true,
                (id) -> {
                    QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("cid", cIdStr);
                    return classMapper.selectOne(queryWrapper);
                });

        return Response.success(ResponseStatus.MANAGE_CLASS_SUCCESS,classInfo);
    }

    public Response getUserById(Long uid) {
        String uIdStr = uid.toString();
        User userInfo = redisUtils.queryForValue(RedisKeyName.MANAGE_USER, uIdStr, User.class, 60L, TimeUnit.DAYS, true,
                (id) -> {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("uid", uIdStr);
                    return userMapper.selectOne(queryWrapper);
                });

        return Response.success(ResponseStatus.MANAGE_USER_SUCCESS,userInfo);
    }
}
