package com.SecondClass.controller;

import com.SecondClass.entity.Organization;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.User;
import com.SecondClass.server.ManageServerImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping(value = "/api/manage")
@RestController
public class ManageController {
    @Resource
    ManageServerImpl manageServer;

    @PostMapping("/login")
    public Response login(@RequestBody R_SignIn request){
        return manageServer.login(request);
    }

    @PostMapping("/createOrg")
    public Response createOrg(@RequestBody Organization request){
        return manageServer.createOrg(request);
    }

    @PostMapping("/addAccount")
    public Response addAccount(@RequestBody User request){
        return manageServer.addAccount(request);
    }

    @GetMapping("/class/{cid}")
    public Response getClassById(@PathVariable("cid")Long cid){
        return manageServer.getClassById(cid);
    }

    @GetMapping("/user/{uid}")
    public Response getUserById(@PathVariable("uid")Long uid){
        return manageServer.getUserById(uid);
    }

}
