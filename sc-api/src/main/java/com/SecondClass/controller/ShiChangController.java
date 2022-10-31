package com.SecondClass.controller;

import com.SecondClass.entity.Response;
import com.SecondClass.entity.ShichangType;

import com.SecondClass.server.ShiChangTypeServer;

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
    ShiChangTypeServer shichangTypeServer;

    @GetMapping(value = "test")
    public Response test(){
        System.out.println("123nknin123");
        return shichangTypeServer.test();
    }
}
