package com.SecondClass.controller;

import com.SecondClass.entity.Organization;
import com.SecondClass.entity.OrganizationApply;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.User;
import com.SecondClass.server.ManageServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/api/manage")
@RestController
public class ManageController {

    @Resource
    ManageServerImpl manageServer;

    @PostMapping("/login")
    public Response login(@RequestBody Map<String,Object> userMap){
        return manageServer.login(userMap);
    }

    @PostMapping("/createOrg")
    public Response createOrg(@RequestBody Organization organization ){
        return manageServer.createOrg(organization );
    }

    @PostMapping("/addAccount")
    public Response addAccount(@RequestBody User user){
        return manageServer.addAccount(user);
    }

    @GetMapping("/getAllOrg/{pageNo}")
    public Response getALlOrg(@PathVariable("pageNo")Integer pageNo){
        return manageServer.getAllOrg(pageNo);
    }

    @GetMapping("/getOrg")
    public Response getOrg(@RequestParam Map<String,Object> orgMap) {
        return manageServer.getOrg(orgMap);
    }

    @GetMapping("/getOrgById/{oid}")
    public Response getOrg(@PathVariable("oid")Long oid) {
        return manageServer.getOrgById(oid);
    }

    @PostMapping("/applyOrg")
    public Response applyOrg(@RequestBody OrganizationApply orgApply) {
        return manageServer.applyOrg(orgApply);
    }

    @PostMapping("/changePwd")
    public Response changePwd(@RequestBody Map<String,Object> pwdMap){
        return manageServer.changePwd(pwdMap);
    }

    //    @PostMapping("/addAccountByBatch")
    //    public Response addAccountByBatch(@RequestBody List<User> userList){return manageServer.addAccountByBatch(userList);}

    @GetMapping("/class/{cid}")
    public Response getClassById(@PathVariable("cid")Long cid){
        return manageServer.getClassById(cid);
    }

    @GetMapping("/user/{uid}")
    public Response getUserById(@PathVariable("uid")Long uid){
        return manageServer.getUserById(uid);
    }

}
