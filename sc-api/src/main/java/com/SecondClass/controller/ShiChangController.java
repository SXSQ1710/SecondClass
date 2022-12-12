package com.SecondClass.controller;

import com.SecondClass.entity.Participation;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.ShichangApplication;
import com.SecondClass.server.IShiChangServer;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @title: ShiChangController
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/10/27 17:31
 **/

@RestController
@RequestMapping(value = "/api/ShiChang")
public class ShiChangController {

    @Resource
    private IShiChangServer shiChangServer;

    @GetMapping("/browseMyShiChang")
    public Response browseMyShiChang(){
        return shiChangServer.browseMyShiChang();
    }

    @GetMapping("/auditActivityShiChang/{aid}/{statue}")
    public Response auditActivityShiChang(@PathVariable("aid") Integer aid,
                                          @PathVariable("statue") Integer statue){
        return shiChangServer.auditActivityShiChang(aid,statue);
    }

    @PostMapping("/shiChangApplication")
    public Response postShiChangApplication(@RequestBody ShichangApplication shichangApplication){
        return shiChangServer.postShiChangApplication(shichangApplication);
    }

    @GetMapping("/shiChangApplication/{pageNo}/{pageSize}")
    public Response getShiChangApplication( @PathVariable("pageNo") Integer pageNo,
                                            @PathVariable("pageSize") Integer pageSize){
        Page<ShichangApplication> page = new Page<>(pageNo,pageSize);
        return shiChangServer.getShiChangApplication(page);
    }

    @GetMapping("/getAllShiChangApplication/{pageNo}/{pageSize}")
    public Response getAllShiChangApplication( @PathVariable("pageNo") Integer pageNo,
                                               @PathVariable("pageSize") Integer pageSize){
        Page<ShichangApplication> page = new Page<>(pageNo,pageSize);
        return shiChangServer.getAllShiChangApplication(page);
    }

    @GetMapping("/getShiAppInfo/{sAppId}")
    public Response getShiAppInfo (@PathVariable("sAppId") Integer sAppId){
        return shiChangServer.getShiAppInfo(sAppId);
    }

    @GetMapping("/getMyParticipation/{uid}/{pageNo}/{pageSize}")
    public Response getMyParticipation(@PathVariable("uid") Integer uid,
                                         @PathVariable("pageNo") Integer pageNo,
                                         @PathVariable("pageSize") Integer pageSize) {
        Page<Participation> page = new Page<>(pageNo,pageSize);
        return shiChangServer.getMyParticipation(uid,page);
    }

    @GetMapping("/queryAllGroupBySid")
    public Response queryAllGroupBySid() {
        return shiChangServer.queryAllGroupBySid();
    }

    @GetMapping("/queryAllGroupBySidAndTime/{startTime}/{endTime}")
    public Response queryAllGroupBySidAndTime(@PathVariable("startTime") Date startTime,
                                              @PathVariable("endTime") Date endTime) {
        return shiChangServer.queryAllGroupBySidAndTime(startTime,endTime);
    }
}
