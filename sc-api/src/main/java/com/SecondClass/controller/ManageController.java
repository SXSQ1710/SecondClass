package com.SecondClass.controller;

import com.SecondClass.entity.Organization;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.User;
import com.SecondClass.server.ManageServerImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping(value = "/api/manage")
@RestController
public class ManageController {
    @Resource
    ManageServerImpl manageServer;

    @PostMapping("/loginIn")
    public Response loginIn(@RequestBody User user){
        return manageServer.loginIn(user);
    }

    @PostMapping("/createOrg")
    public Response createOrg(@RequestBody Organization request){
        return manageServer.createOrg(request);
    }

    @PostMapping("/addAccount")
    public Response addAccount(@RequestBody User request){
        return manageServer.addAccount(request);
    }



}
