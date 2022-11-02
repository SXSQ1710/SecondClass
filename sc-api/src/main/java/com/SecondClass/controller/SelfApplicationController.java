package com.SecondClass.controller;

import com.SecondClass.entity.Response;
import com.SecondClass.entity.SelfApplication;
import com.SecondClass.server.ISelfApplicationServer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/selfApplication")
public class SelfApplicationController {

    @Resource
    private ISelfApplicationServer selfApplicationServer;

    @PostMapping("/addSelfApplication")
    public Response applySelfApplication(@RequestBody SelfApplication selfApplication){
        System.out.println(selfApplication.getSelfAppStatu());
        return selfApplicationServer.addSelfApplication(selfApplication);
    }

    @PostMapping("/addSelfApplicationAttachment")
    public Response addSelfApplicationAttachment(@RequestParam MultipartFile attachment){
        return selfApplicationServer.addSelfApplicationAttachment(attachment);
    }

}
