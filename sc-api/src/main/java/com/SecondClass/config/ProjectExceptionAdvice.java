package com.SecondClass.config;


import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @title: ProjectExceptionAdvice
 * @Author SXSQ
 * @Description //TODO 全局异常处理
 * @Date 2022/10/9 12:51
 **/
@RestControllerAdvice
@Slf4j
public class ProjectExceptionAdvice {

    @ExceptionHandler(org.springframework.data.redis.RedisConnectionFailureException.class)
    public void RedisConnectionFailureException(Exception e){
        log.error("全局异常捕获：redis链接失败！" + e.toString());
    }

    @ExceptionHandler(cn.dev33.satoken.exception.NotLoginException.class)
    public Response NotLoginException(Exception e){
        log.error("全局异常捕获：NotLoginException！");
        return Response.error(ResponseStatus.USER_NOT_LOGIN_FAIL);
    }

    @ExceptionHandler(cn.dev33.satoken.exception.NotPermissionException.class)
    public Response NotPermissionException(Exception e){
        log.error("全局异常捕获：NotPermissionException！");
        return Response.error(ResponseStatus.MANAGE_PERMISSION);
    }

    @ExceptionHandler(cn.dev33.satoken.exception.NotRoleException.class)
    public Response NotRoleException(Exception e){
        log.error("全局异常捕获：NotRoleException！");
        return Response.error(ResponseStatus.MANAGE_PERMISSION);
    }
}
