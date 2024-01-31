package com.SecondClass.server.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.SecondClass.entity.R_entity.R_Login;
import com.SecondClass.entity.R_entity.R_Student;
import com.SecondClass.entity.R_entity.R_UserAppOrg;
import com.SecondClass.mapper.*;
import com.SecondClass.server.IManageServer;
import com.SecondClass.utils.ExcelListener;
import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.SecondClass.entity.*;
import com.SecondClass.entity.Class;
import com.SecondClass.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
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
    @Resource
    ShiChangTypeMapper shichangTypeMapper;

//    @Resource
//    IManageServer manageserver;



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
     * @Description //获取所有的用户
     * @Date 18:18 2022/11/12
     * @Param [pageNo]
     * @return com.SecondClass.entity.Response
     **/
    @Override
    public Response getAllAccount(Page<User> page) {
        IPage<User> orgPage = userMapper.selectPage(page, null);
        orgPage.getRecords().forEach((value) -> {
            value.setUpassword(null);
            value.setOid(null);
        });
        if (orgPage.getTotal() == 0) return Response.success(ResponseStatus.MANAGE_USER_FAIL);
        return Response.success(ResponseStatus.MANAGE_USER_SUCCESS, orgPage);
    }

    /**
     * @Author jiang
     * @Description //获取所有的组织
     * @Date 18:18 2022/11/12
     * @Param [pageNo]
     * @return com.SecondClass.entity.Response
     **/
    @Override
    public Response getAllOrg(Page<Organization> page) {
        IPage<Organization> orgPage = organizationMapper.selectPage(page,null);
        if(orgPage.getTotal()==0)return Response.success(ResponseStatus.ORGANIZATION_QUERY_FAIL);
        return Response.success(ResponseStatus.ORGANIZATION_QUERY_SUCCESS,orgPage);
    }

    /**
     * @Author jiang
     * @Description //根据提供的信息查询组织
     * @Date 22:10 2022/11/12
     * @Param [orgMap]
     * @return com.SecondClass.entity.Response
     **/
    @Override
    public Response getOrg(Organization organization) {
        try {
            LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Organization::getOname,organization.getOname()).or()
                        .like(Organization::getOdescription,organization.getOdescription()).or()
                        .like(Organization::getCampus,organization.getCampus());
            List<Organization> organizationList = organizationMapper.selectList(queryWrapper);
            organizationList.forEach((value) -> {
                value.setPermissionsLevel(null);
                value.setUid(null);
            });
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
        organization.setPermissionsLevel(null);
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
            Long uid = Long.parseLong((String)StpUtil.getLoginId()) ;
            orgApply.setUid(uid);
            String oid = orgApply.getOid().toString();
            Organization organization = redisUtils.queryForValue(RedisKeyName.MANAGE_ORGANIZATION, oid, Organization.class, 60L, TimeUnit.DAYS, true,
                    (id) -> {
                        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(Organization::getOid, oid);
                        return organizationMapper.selectOne(queryWrapper);
                    });
            //查看组织是否存在
            if (!BeanUtil.isNotEmpty(organization)) return Response.error(ResponseStatus.ORGANIZATION_APPLY_FAIL);

            String oidListStr = (String) StpUtil.getSession().get("o");
            List<Integer> oidList = JSONUtil.toList(oidListStr, Integer.class);
            if(oidList.contains(orgApply.getOid().intValue())) return Response.error(ResponseStatus.ORGANIZATION_APPLY_FAIL_1);
            orgApply.setOAppStatus(0);
            int i = organizationApplyMapper.insert(orgApply);
            if (i==1) {
                redisUtils.setValue(RedisKeyName.MANAGE_ORGANIZATION_APP + orgApply.getOAppId().toString(), orgApply);
                return Response.success(ResponseStatus.ORGANIZATION_APPLY_SUCCESS);
            }else {
                return Response.success(ResponseStatus.ORGANIZATION_APPLY_FAIL);
            }
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
    public Response getApplyOrg(Page<OrganizationApply> page) {
        //获取负责人的管理的oid
        String uid =(String) StpUtil.getLoginId();
        String oidListStr = (String) StpUtil.getSession().get("o");
        List<Integer> oidList = JSONUtil.toList(oidListStr, Integer.class);
        for (int i = 0; i < oidList.size(); i++) {
            LambdaQueryWrapper<OrganizationMember> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrganizationMember::getOid, oidList.get(i))
                    .eq(OrganizationMember::getUid, uid);
            OrganizationMember organizationMember = organizationMemberMapper.selectOne(wrapper);
            if (!organizationMember.getPosition().equals("负责人")) oidList.remove(i);
        }
        if (oidList.size() == 0) return Response.error(ResponseStatus.ORGANIZATION_APP_QUERY_FAIL);

        QueryWrapper<OrganizationApply> organizationApplyQueryWrapper = new QueryWrapper<>();
        organizationApplyQueryWrapper.in("oid",oidList);
        IPage<OrganizationApply> orgApplyPage = organizationApplyMapper.selectPage(page,organizationApplyQueryWrapper);
        List<OrganizationApply> organizationApplyList = orgApplyPage.getRecords();
        if(organizationApplyList.size()==0)return Response.success(ResponseStatus.ORGANIZATION_APP_QUERY_FAIL);

        ArrayList<R_UserAppOrg> r_userAppOrgs = new ArrayList<>();
        for (int i = 0; i < organizationApplyList.size(); i++) {
            String auid = organizationApplyList.get(i).getUid().toString();
            User user = redisUtils.queryForValue(RedisKeyName.MANAGE_USER, auid, User.class, 60L, TimeUnit.DAYS, false,
                    (id) -> {
                        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(User::getUid, uid);
                        return userMapper.selectOne(queryWrapper);
                    });

            String cIdStr = user.getCid().toString();
            Class classInfo = redisUtils.queryForValue(RedisKeyName.MANAGE_CLASS, cIdStr, Class.class, 60L, TimeUnit.DAYS, true,
                    (id) -> {
                        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("cid", cIdStr);
                        return classMapper.selectOne(queryWrapper);
                    });

            String oid = organizationApplyList.get(i).getOid().toString();
            Organization organization = redisUtils.queryForValue(RedisKeyName.MANAGE_ORGANIZATION, oid, Organization.class, 60L, TimeUnit.DAYS, true,
                    (id) -> {
                        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("oid", oid);
                        return organizationMapper.selectOne(queryWrapper);
                    });
            R_UserAppOrg r_student = BeanUtil.copyProperties(user, R_UserAppOrg.class);
            r_student.setCampus(classInfo.getCampus());
            r_student.setCname(classInfo.getCname());
            r_student.setCollege(classInfo.getCollege());
            r_student.setMajor(classInfo.getMajor());
            r_student.setAppOid(organization.getOid());
            r_student.setAppOname(organization.getOname());

            r_userAppOrgs.add(r_student);
        }
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("records",r_userAppOrgs);
        stringObjectHashMap.put("total",orgApplyPage.getTotal());
        stringObjectHashMap.put("size",orgApplyPage.getSize());
        stringObjectHashMap.put("pages",orgApplyPage.getPages());
        stringObjectHashMap.put("current",orgApplyPage.getCurrent());
        return Response.success(ResponseStatus.ORGANIZATION_APP_QUERY_SUCCESS,stringObjectHashMap);
    }

    @Override
    @Transactional
    public Response auditOrgApp(Long oAppId, Integer oAppStatus) {
        try {
            //获得申请实体
            OrganizationApply orgApplication = redisUtils.queryForValue(RedisKeyName.MANAGE_ORGANIZATION_APP, oAppId.toString(), OrganizationApply.class, 60L, TimeUnit.DAYS, true,
                    (id) -> {
                        LambdaQueryWrapper<OrganizationApply> orgAppWrapper = new LambdaQueryWrapper<>();
                        orgAppWrapper
                                .eq(OrganizationApply::getOAppId, oAppId);
                        return organizationApplyMapper.selectOne(orgAppWrapper);
                    });

            if (!BeanUtil.isNotEmpty(orgApplication)) throw new IllegalArgumentException();
            if (orgApplication.getOAppStatus() != 0) throw new IllegalArgumentException();

            //更新申请表
            LambdaUpdateWrapper<OrganizationApply> organizationApplyUpdateWrapper = new LambdaUpdateWrapper<>();
            organizationApplyUpdateWrapper
                    .eq(OrganizationApply::getOAppId, oAppId)
                    .set(OrganizationApply::getOAppStatus, oAppStatus);

            int i2 = 0;
            int i3 = 0;
            int i = organizationApplyMapper.update(null, organizationApplyUpdateWrapper);
            //查看是否已经是组织成员,如果已经是成员，重复申请，直接返回成功
//            QueryWrapper<OrganizationMember> organizationMemberQueryWrapper = new QueryWrapper<>();
//            organizationMemberQueryWrapper.eq("oid",orgApplication.getOid())
//                    .eq("uid",orgApplication.getUid());
//            OrganizationMember temp_member = organizationMemberMapper.selectOne(organizationMemberQueryWrapper);
//            if(temp_member != null){
//                return Response.success(ResponseStatus.ORGANIZATION_APPLY_AUDIT_SUCCESS);
//            }
            //更新成员表和user表
            if (i == 1 && oAppStatus == 2) {
                //更新user表
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(User::getUid, orgApplication.getUid());
                User user = userMapper.selectOne(queryWrapper);
                String jsonStr = user.getOid();
                List<Integer> oidList = JSONUtil.toList(jsonStr, Integer.class);
                oidList.add(orgApplication.getOid().intValue());
                user.setOid(JSONUtil.toJsonStr(oidList));
                i3 = userMapper.updateById(user);
                //删除缓存
                stringRedisTemplate.delete(RedisKeyName.MANAGE_USER + user.getUid().toString());
                //更新成员表
                OrganizationMember member = new OrganizationMember();
                member.setPosition("普通成员");
                member.setOid(orgApplication.getOid());
                member.setUid(orgApplication.getUid());
                i2 = organizationMemberMapper.insert(member);
                //删除缓存
                stringRedisTemplate.delete(RedisKeyName.MANAGE_ORGANIZATION_MEMBER + orgApplication.getOid().toString());
            }
            if (i == 1 && i2 == 1 && i3 == 1) {
                stringRedisTemplate.delete(RedisKeyName.MANAGE_ORGANIZATION_APP + oAppId.toString());
                return Response.success(ResponseStatus.ORGANIZATION_APPLY_AUDIT_SUCCESS);
            }
            else return Response.success(ResponseStatus.ORGANIZATION_APPLY_AUDIT_FAIL);
        } catch (DataIntegrityViolationException d){
            //数据库插入失败
            return Response.error(ResponseStatus.ORGANIZATION_APPLY_AUDIT_FAIL);
        }catch (Exception e) {
            //其他错误
            return Response.error(ResponseStatus.ORGANIZATION_APPLY_AUDIT_FAIL);
        }
    }

    @Override
    public Response getMember(Page<MemberView> page) {
        String uid =(String) StpUtil.getLoginId();
        QueryWrapper<Organization> orgWrapper = new QueryWrapper<>();
        orgWrapper.eq("uid",uid);
        List<Organization> list = organizationMapper.selectList(orgWrapper);
        if(list.size()==0)return Response.success(ResponseStatus.MEMBER_QUERY__FAIL);
        ArrayList<String> oids = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String oid = String.valueOf(list.get(i).getOid());
            //查询对应oid的申请信息
            oids.add(oid);
        }

        QueryWrapper<MemberView> memberViewQueryWrapper= new QueryWrapper<>();
        memberViewQueryWrapper.in("oid",oids);
        Page<MemberView> memberViewPage = memberViewMapper.selectPage(page, memberViewQueryWrapper);
        return Response.success(ResponseStatus.MEMBER_QUERY_SUCCESS,memberViewPage);
    }

    @Override
    public Response getMemberByOid(Integer oid) {
        LambdaQueryWrapper<MemberView> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberView::getOid, oid);
        List<MemberView> memberViews = memberViewMapper.selectList(wrapper);

        return Response.success(ResponseStatus.SUCCESS, memberViews);
    }

    /**
     * @Author jiang
     * @Description 参考文档 https://blog.csdn.net/weixin_46146718/article/details/115250530
     * @Date 17:53 2022/11/12
     * @Param MultipartFile
     * @return com.SecondClass.entity.Response
     **/

    @Transactional
    public Response addAccountByBatch(MultipartFile file) {
        try {
            //获取文件名
            String filename = file.getOriginalFilename();
            System.out.println("filename:"+filename);
            //获取文件流
            InputStream inputStream = file.getInputStream();
            //实例化实现了AnalysisEventListener接口的类
            ExcelListener listener = new ExcelListener();
            //封装数据
            EasyExcelFactory.read(inputStream, User.class, listener).headRowNumber(1).build().readAll();
            //获取数据列表
            List<User> userList = listener.getDatas();
            //批量插入数据到user表中
            saveBatch(userList);

            return Response.success(ResponseStatus.CREATE_USERBYBATCH_SUCCESS);
        } catch (DuplicateKeyException e) {
            return Response.error(ResponseStatus.CREATE_USERBYBATCH_FAIL_1);
        } catch (Exception e){
            e.printStackTrace();
            return Response.error(ResponseStatus.CREATE_USERBYBATCH_FAIL);
        }

    }

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

    public Response isLogin() {
        try{
            String uid =(String) StpUtil.getLoginId();
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            Map<String, String> token = new HashMap<>();
            token.put("tokenName",tokenInfo.tokenName);
            token.put("tokenValue",tokenInfo.tokenValue);
            return Response.success(ResponseStatus.SUCCESS,token);
        }catch (Exception e){
            return Response.error(ResponseStatus.FAIL);
        }
    }
}
