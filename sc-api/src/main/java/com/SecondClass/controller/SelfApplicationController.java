package com.SecondClass.controller;

import com.SecondClass.entity.Response;
import com.SecondClass.entity.SelfApplication;
import com.SecondClass.server.ISelfApplicationServer;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        return selfApplicationServer.addSelfApplication(selfApplication);
    }

    @PostMapping("/addSelfApplicationAttachment")
    public Response addSelfApplicationAttachment(@RequestParam MultipartFile attachment){
        return selfApplicationServer.addSelfApplicationAttachment(attachment);
    }

    @GetMapping("/auditSelfApplication/{selfAppId}/{statue}")
    public Response auditSelfApplication(@PathVariable("selfAppId") Integer selfAppId,
                                         @PathVariable("statue") Integer statue) {
        return selfApplicationServer.auditSelfApplication(selfAppId, statue);
    }

    @GetMapping("/getMySelfApplication/{uid}/{pageNo}/{pageSize}")
    public Response getMySelfApplication(@PathVariable("uid") Integer uid,
                                         @PathVariable("pageNo") Integer pageNo,
                                         @PathVariable("pageSize") Integer pageSize) {
        Page<SelfApplication> page = new Page<>(pageNo,pageSize);
        return selfApplicationServer.getMySelfApplication(uid,page);
    }

    @PostMapping("/updateSelfApplication")
    public Response updateSelfApplication(@RequestBody SelfApplication selfApplication){
        return selfApplicationServer.updateSelfApplication(selfApplication);
    }



}
