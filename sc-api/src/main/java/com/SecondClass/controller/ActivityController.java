package com.SecondClass.controller;


import com.SecondClass.entity.*;
import com.SecondClass.entity.R_entity.R_ActivityApplication;

import com.SecondClass.server.ActivityServerImpl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping (value = "/api/activity")
public class ActivityController {
    @Resource
    ActivityServerImpl activityServer;

    @PostMapping("/applyActivity")
    public Response applyActivity(@RequestBody R_ActivityApplication request){
        return activityServer.applyActivity(request);
    }

    @GetMapping("/auditActivity")
    public Response auditActivity(Integer aAppId, Integer status, String explain){
        return activityServer.auditActivity(aAppId,status,explain);
    }

    @GetMapping("/findActivityAppByUid")
    public Response findActivityAppByUid(Long uid,Integer pageNo,Integer pageSize){
        Page<ActivityApplication> page = new Page<>(pageNo,pageSize);
        return activityServer.findActivityAppByUid(uid,page);
    }

    @GetMapping("/findAppStatusByAAppid")
    public Response findAppStatusByAid(Long aAppId ){
        return activityServer.findAppStatusByAid(aAppId);
    }

    @GetMapping("/getAllApp")
    public Response getAllApp(Integer pageNo,Integer pageSize){
        Page<ActivityApplication> page = new Page<>(pageNo,pageSize);
        return activityServer.getAllApp(page);
    }

    @GetMapping("/getAll")
    public Response getAll(Integer pageNo,Integer pageSize){
        Page<Activity> page = new Page<>(pageNo,pageSize);
        return activityServer.getAll(page);
    }

    @GetMapping("/findActivityByAid")
    public Response findActivityByAid(Long aid){
        return activityServer.findActivityByAid(aid);
    }

    @PostMapping("/register")
    public Response register(@RequestBody Participation participation){
        return activityServer.register(participation);
    }

    @PostMapping("/signIn")
    public Response signIn(@RequestBody Participation participation){
        return activityServer.signIn(participation);
    }

    @PostMapping("/signOff")
    public Response signOff(@RequestBody Participation participation){
        return activityServer.signOff(participation);
    }

    @GetMapping("/getAllRegisteredUser")
    public Response getAllRegisteredUser(Long aid,Integer pageNo,Integer pageSize){
        Page<User> page = new Page<>(pageNo,pageSize);
        return activityServer.getAllRegisteredUser(aid,page);
    }

    @PostMapping("/modifyRegisterStatusByUid")
    public Response modifyRegisterStatusByUid(@RequestBody Participation participation){
        return activityServer.modifyRegisterStatusByUid(participation);
    }

}
