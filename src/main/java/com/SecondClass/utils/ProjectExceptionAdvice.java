package com.SecondClass.utils;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @title: ProjectExceptionAdvice
 * @Author SXSQ
 * @Description //TODO 全局异常处理
 * @Date 2022/10/9 12:51
 **/
@RestControllerAdvice
public class ProjectExceptionAdvice {

    @ExceptionHandler(org.springframework.data.redis.RedisConnectionFailureException.class)
    public void RedisConnectionFailureException(Exception e){
        System.out.println("全局异常捕获：redis链接失败！" + e.toString());
    }


}
