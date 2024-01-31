package com.SecondClass.controller;

import com.SecondClass.entity.R_entity.R_ActivityApplication;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import com.SecondClass.scheduled.DataBaseClear;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @title: TestController
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/10/3 21:06
 **/

@RestController
@RequestMapping(value = "/api/dataBaseClear")
public class TestController {

    @Resource
    DataBaseClear dataBaseClear;

    public static CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();

    @GetMapping("/participation/{grade}")
    public Response dataBaseClear(@PathVariable("grade") Long grade) {
        // todo 需要使用锁优化
        dataBaseClear.dataBaseClear(grade);
        return Response.success(ResponseStatus.SUCCESS, "操作成功！");
    }

    @GetMapping("/initCopy")
    public Response initCopy() {
        copyOnWriteArrayList.add("111");
        copyOnWriteArrayList.add("222");
        copyOnWriteArrayList.add("333");
        copyOnWriteArrayList.add("444");
        return Response.success(ResponseStatus.SUCCESS, copyOnWriteArrayList);
    }

    @GetMapping("/addCopy/{data}")
    public Response addCopy(@PathVariable("data") String data) {
        copyOnWriteArrayList.add(data);
        return Response.success(ResponseStatus.SUCCESS, copyOnWriteArrayList);
    }
}
