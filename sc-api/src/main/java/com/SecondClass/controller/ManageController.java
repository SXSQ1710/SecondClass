package com.SecondClass.controller;

import com.SecondClass.entity.Organization;
import com.SecondClass.entity.R_entity.R_SignIn;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.User;
import com.SecondClass.server.ManageServerImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RequestMapping(value = "/api/manage")
@RestController
public class ManageController {
    @Resource
    ManageServerImpl manageServer;

    @PostMapping("/login")
    public Response login(@RequestBody Map<String, Object> userMap){
        return manageServer.login(userMap);
    }

    @PostMapping("/createOrg")
    public Response createOrg(@RequestBody Organization org){
        return manageServer.createOrg(org);
    }

    @PostMapping("/addAccount")
    public Response addAccount(@RequestBody User request){
        return manageServer.addAccount(request);
    }



}
