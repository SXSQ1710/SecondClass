package com.SecondClass.controller;


import com.SecondClass.entity.*;
import com.SecondClass.entity.R_entity.R_ActivityApplication;

import com.SecondClass.entity.R_entity.R_ShiAppInfo;
import com.SecondClass.entity.R_entity.R_SignIn;
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

    @GetMapping("/signIn/{aid}/{uid}/{type}")
    public Response getSignIn(@PathVariable("aid")Long aid, @PathVariable("uid")Long uid, @PathVariable("type")Integer type){
        return activityServer.getSignIn(aid, uid, type);
    }

    @PostMapping("/signInOrOff")
    public Response signInOrOff(@RequestBody R_SignIn signIn){
        return activityServer.signInOrOff(signIn);
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

    @GetMapping("/getAllParticipatedMember/{aid}")
    public Response getAllParticipatedMember(@PathVariable("aid")Long aid){
        return activityServer.getAllParticipatedMember(aid);
    }

    @GetMapping("/searchByName/{aname}/{pageNo}/{pageSize}")
    public Response searchByName(@PathVariable("aname")String aname,@PathVariable("pageNo")Integer pageNo,@PathVariable("pageSize")Integer pageSize){
        Page<Activity> page = new Page<>(pageNo,pageSize);
        return activityServer.searchByName(aname,page);
    }

    /**
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/getParticipationByUid/{uid}/{pageNo}/{pageSize}")
    public Response getParticipationByUid(@PathVariable("uid")String uid,@PathVariable("pageNo")Integer pageNo,@PathVariable("pageSize")Integer pageSize){
        Page<Participation> page = new Page<>(pageNo,pageSize);
        return activityServer.getParticipationByUid(uid,page);
    }

    @PostMapping("/sendInfoToOrganization")
    public Response sendInfoToOrganization(@RequestBody R_ShiAppInfo info){

        return activityServer.sendInfoToOrganization(info);
    }
}
