package com.SecondClass.controller;

import com.SecondClass.entity.R_entity.R_ActicityApplication;
import com.SecondClass.entity.Response;
import com.SecondClass.server.ActivityServerImpl;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping (value = "/api/activity")
public class ActivityController {
    @Resource
    ActivityServerImpl activityServer;

    @PostMapping("/applyActivity")
    public Response applyActivity(@RequestBody R_ActicityApplication request){
        return activityServer.applyActivity(request);
    }

    @GetMapping("/auditActivity")
    public Response auditActivity(Integer aAppId, Integer status, String explain){
        System.out.println(explain);
        return activityServer.auditActivity(aAppId,status,explain);
    }
}
