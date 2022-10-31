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

    @GetMapping(value = {"/auditActivity/{aAppId}/{status}/{explain}","/auditActivity/{aAppId}/{status}"})
    public Response auditActivity(@PathVariable("aAppId")Integer aAppId, @PathVariable("status")Integer status,@PathVariable(value = "explain",required = false) String explain){
        return activityServer.auditActivity(aAppId,status,explain);
    }

    @GetMapping("/findActivityAppByUid/{uid}/{pageNo}/{pageSize}")
    public Response findActivityAppByUid(@PathVariable("uid")Long uid,@PathVariable("pageNo")Integer pageNo,@PathVariable("pageSize")Integer pageSize){
        Page<ActivityApplication> page = new Page<>(pageNo,pageSize);
        return activityServer.findActivityAppByUid(uid,page);
    }

    @GetMapping("/findAppStatusByAAppid/{aAppId}")
    public Response findAppStatusByAid(@PathVariable("aAppId") Long aAppId ){
        return activityServer.findAppStatusByAid(aAppId);
    }

    @GetMapping("/getAllApp/{pageNo}/{pageSize}")
    public Response getAllApp(@PathVariable("pageNo")Integer pageNo,@PathVariable("pageSize")Integer pageSize){
        Page<ActivityApplication> page = new Page<>(pageNo,pageSize);
        return activityServer.getAllApp(page);
    }

    @GetMapping("/getAll/{pageNo}/{pageSize}")
    public Response getAll(@PathVariable("pageNo")Integer pageNo,@PathVariable("pageSize")Integer pageSize){
        Page<Activity> page = new Page<>(pageNo,pageSize);
        return activityServer.getAll(page);

    }

    @GetMapping("/findActivityByAid/{aid}")
    public Response findActivityByAid(@PathVariable("aid") Long aid){
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

    @GetMapping("/getAllRegisteredUser/{aid}/{pageNo}/{pageSize}")
    public Response getAllRegisteredUser(@PathVariable("aid")Long aid,@PathVariable("pageNo")Integer pageNo,@PathVariable("pageSize")Integer pageSize){
        Page<User> page = new Page<>(pageNo,pageSize);
        return activityServer.getAllRegisteredUser(aid,page);
    }

    @PostMapping("/modifyRegisterStatusByUid")
    public Response modifyRegisterStatusByUid(@RequestBody Participation participation){
        return activityServer.modifyRegisterStatusByUid(participation);
    }

}
