package com.SecondClass.controller;

import com.SecondClass.entity.Response;
import com.SecondClass.server.ShiChangServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    ShiChangServer shiChangServer;

    @GetMapping(value = "test")
    public Response test(){
        return shiChangServer.test();
    }
}
