package com.SecondClass.controller;

import com.SecondClass.entity.*;
import com.SecondClass.entity.R_entity.R_Login;
import com.SecondClass.server.ManageServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/api/manage")
@RestController
public class ManageController {

    @Resource
    ManageServerImpl manageServer;

    @PostMapping("/login")
    public Response login(@RequestBody R_Login rLogin){ return manageServer.login(rLogin); }

    @PostMapping("/createOrg")
    public Response createOrg(@RequestBody Organization organization ){
        return manageServer.createOrg(organization );
    }

    @PostMapping("/addAccount")
    public Response addAccount(@RequestBody User user){
        return manageServer.addAccount(user);
    }

    @GetMapping("/getAllAccount/{pageNo}/{pageSize}")
    public Response getAllAccount(@PathVariable("pageNo") Integer pageNo,
                                  @PathVariable("pageSize") Integer pageSize){
        Page<User> page = new Page<>(pageNo, pageSize);
        return manageServer.getAllAccount(page);
    }

    @GetMapping("/getAllOrg/{pageNo}/{pageSize}")
    public Response getALlOrg( @PathVariable("pageNo") Integer pageNo,
                               @PathVariable("pageSize") Integer pageSize){
        Page<Organization> page = new Page<>(pageNo,pageSize);
        return manageServer.getAllOrg(page);
    }

    @GetMapping("/getApplyOrg/{pageNo}/{pageSize}")
    public Response getApplyOrg( @PathVariable("pageNo") Integer pageNo,
                                 @PathVariable("pageSize") Integer pageSize){
        Page<OrganizationApply> page = new Page<>(pageNo,pageSize);
        return manageServer.getApplyOrg(page);
    }

    @GetMapping("/getOrg")
    public Response getOrg(@RequestBody Organization organization) {
        return manageServer.getOrg(organization);
    }

    @GetMapping("/getOrgById/{oid}")
    public Response getOrg(@PathVariable("oid")Long oid) {
        return manageServer.getOrgById(oid);
    }

    @PostMapping("/applyOrg")
    public Response applyOrg(@RequestBody OrganizationApply orgApply) {
        return manageServer.applyOrg(orgApply);
    }

    @GetMapping("/auditOrgApp/{oAppId}/{oAppStatus}")
    public Response auditOrgApp(@PathVariable("oAppId")Long oAppId,@PathVariable("oAppStatus")Integer oAppStatus) {
        return manageServer.auditOrgApp(oAppId,oAppStatus);
    }

    @GetMapping("/getMember/{pageNo}/{pageSize}")
    public Response getMember( @PathVariable("pageNo") Integer pageNo,
                               @PathVariable("pageSize") Integer pageSize) {
        Page<MemberView> page = new Page<>(pageNo,pageSize);
        return manageServer.getMember(page);
    }

    @GetMapping("/getMemberByOid/{oid}")
    public Response getMemberByOid(@PathVariable("oid") Integer oid){
        return manageServer.getMemberByOid(oid);
    }

    @PostMapping("/changePwd")
    public Response changePwd(@RequestBody Map<String,Object> pwdMap){
        return manageServer.changePwd(pwdMap);
    }

    @PostMapping("/addAccountByBatch")
    public Response addAccountByBatch(MultipartFile file){return manageServer.addAccountByBatch(file);}

    @GetMapping("/class/{cid}")
    public Response getClassById(@PathVariable("cid")Long cid){
        return manageServer.getClassById(cid);
    }

    @GetMapping("/user/{uid}")
    public Response getUserById(@PathVariable("uid")Long uid){
        return manageServer.getUserById(uid);
    }

}
