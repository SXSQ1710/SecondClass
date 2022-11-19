package com.SecondClass.server;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.SecondClass.entity.R_entity.R_Login;
import com.SecondClass.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.SecondClass.entity.*;
import com.SecondClass.entity.Class;
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
import java.util.HashMap;
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
public class ManageServerImpl extends ServiceImpl<UserMapper,User> implements IManageServer {

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
    @Resource
    OrganizationApplyMapper organizationApplyMapper;
    @Resource
    OrganizationMemberMapper organizationMemberMapper;
    @Resource
    MemberViewMapper memberViewMapper;
//    @Resource
//    ManageServer manageserver;



    /**
     * @Author jiang
     * @Description //登录
     * @Date 15:18 2022/11/12
     * @Param [userMap]
     * @return com.SecondClass.entity.Response
     **/
    public Response login(R_Login rLogin) {
        try {
            //1.查询登录信息
            String uid = rLogin.getUid().toString();
            String upassword = rLogin.getUpassword().toString();

            User user = redisUtils.queryForValue(RedisKeyName.MANAGE_USER, uid, User.class, 60L, TimeUnit.DAYS, false,
                    (id) -> {
                        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(User::getUid, uid)
                                .eq(User::getUpassword, upassword);
                        return userMapper.selectOne(queryWrapper);
                    });

            //1.1查询失败返回错误信息
            if (user == null || !user.getUpassword().equals(upassword)) return Response.success(ResponseStatus.USER_LOGIN_FAIL);

            List<Integer> oidList = JSONUtil.toList(user.getOid(), Integer.class);
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

                redisUtils.setValue(RedisKeyName.MANAGE_ORGANIZATION_LEVEL + oid, String.valueOf(permission));
                if (permission < level) level = permission;
            }

            if (rLogin.getRole() == 1 && !(level <= 2)) return Response.error(ResponseStatus.USER_LOGIN_FAIL_1);
            //2.登录成功
            //2.1 saToke登记用户信息
            StpUtil.login(uid);
            //2.2 saToken保存用户组织
            StpUtil.getSession(true).set("o", user.getOid());
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            Map<String, String> token = new HashMap<>();
            token.put("tokenName",tokenInfo.tokenName);
            token.put("tokenValue",tokenInfo.tokenValue);

            return Response.success(ResponseStatus.USER_LOGIN_SUCCESS,token);
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
            int i2 = 0;
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
                //插入orgMember表
                OrganizationMember member = new OrganizationMember();
                member.setOid(organization.getOid());
                member.setUid(organization.getUid());
                member.setPosition("负责人");
                i2 = organizationMemberMapper.insert(member);
            }else return Response.success(ResponseStatus.CREATE_ORGANIZATION_FAIL);

            if(i1 == 1 && i2 ==1) return Response.success(ResponseStatus.CREATE_ORGANIZATION_SUCCESS);
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
    @Transactional
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
    @Transactional
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
        try {
            Long uid = (Long) StpUtil.getLoginId();
            orgApply.setUid(uid);
            int i = organizationApplyMapper.insert(orgApply);
            if (i==1)return Response.success(ResponseStatus.ORGANIZATION_APPLY_SUCCESS);
            else return Response.success(ResponseStatus.ORGANIZATION_APPLY_FAIL);
        } catch (DataIntegrityViolationException d){
            //数据库插入失败
            d.printStackTrace();
            return Response.error(ResponseStatus.ORGANIZATION_APPLY_FAIL);
        }catch (Exception e) {
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }

    @Override
    public Response getApplyOrg(int pageNo) {
        Page<OrganizationApply> page = new Page<>(pageNo,15);
        IPage<OrganizationApply> orgApplyPage = organizationApplyMapper.selectPage(page,null);
        List<OrganizationApply> organizationApplyList = orgApplyPage.getRecords();
        if(organizationApplyList.size()==0)return Response.success(ResponseStatus.ORGANIZATION_APP_QUERY_FAIL);
        return Response.success(ResponseStatus.ORGANIZATION_APP_QUERY_SUCCESS,organizationApplyList);
    }

    @Override
    public Response auditOrgApp(Long oAppId, Integer oAppStatus) {
        try {
            //获得申请实体
            QueryWrapper<OrganizationApply> orgAppWrapper = new QueryWrapper<>();
            orgAppWrapper.eq("oAppId", oAppId);
            OrganizationApply orgApplication = organizationApplyMapper.selectOne(orgAppWrapper);
            if (orgApplication == null) throw new IllegalArgumentException();

            //更新申请表
            UpdateWrapper<OrganizationApply> organizationApplyUpdateWrapper = new UpdateWrapper<>();
            organizationApplyUpdateWrapper.eq("oAppId", oAppId).set("oAppStatus", oAppStatus);
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            i = organizationApplyMapper.update(null, organizationApplyUpdateWrapper);
            //更新成员表
            if (i == 1 && oAppStatus == 2) {
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(User::getUid, orgApplication.getUid());
                User user = userMapper.selectOne(queryWrapper);
                String jsonStr = user.getOid();
                List<Integer> oidList = JSONUtil.toList(jsonStr, Integer.class);
                oidList.add(orgApplication.getOid().intValue());
                user.setOid(JSONUtil.toJsonStr(oidList));
                i3 = userMapper.updateById(user);
                OrganizationMember member = new OrganizationMember();
                member.setPosition("普通成员");
                member.setOid(orgApplication.getOid());
                member.setUid(orgApplication.getUid());
                i2 = organizationMemberMapper.insert(member);
            }
            if (i == 1 && i2 == 1 && i3 == 1) return Response.success(ResponseStatus.ORGANIZATION_APPLY_AUDIT_SUCCESS);
            else return Response.success(ResponseStatus.ORGANIZATION_APPLY_AUDIT_FAIL);
        } catch (DataIntegrityViolationException d){
        //数据库插入失败
        d.printStackTrace();
        return Response.error(ResponseStatus.ORGANIZATION_APPLY_AUDIT_FAIL);
    }catch (Exception e) {
        //其他错误
        e.printStackTrace();
        return Response.success(ResponseStatus.ERROR);
    }
    }

    @Override
    public Response getMember() {
        String uid =(String) StpUtil.getLoginId();
        QueryWrapper<Organization> orgWrapper = new QueryWrapper<>();
        orgWrapper.eq("uid",uid);
        Long oid = (organizationMapper.selectOne(orgWrapper)).getOid();
        QueryWrapper<MemberView> memberViewQueryWrapper= new QueryWrapper<>();
        memberViewQueryWrapper.eq("oid",oid);
        List<MemberView> memberViewList = memberViewMapper.selectList(memberViewQueryWrapper);
        if(memberViewList.size()==0)return Response.success(ResponseStatus.MEMBER_QUERY__FAIL);
        return Response.success(ResponseStatus.MEMBER_QUERY_SUCCESS,memberViewList);
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
