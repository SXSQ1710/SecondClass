package com.SecondClass.controller;

import com.SecondClass.entity.Activity;
import com.SecondClass.entity.Response;
import com.SecondClass.server.ActivityServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping (value = "/api/activity")
public class ActivityController {
    @Resource
    ActivityServer activityServer;

    @PostMapping("/applyActivity")
    public Response applyActivity(@RequestBody Activity activity){
        System.out.println(activity);
        return null;
//        return activity.applyActivity(activity);
    }
}
