package com.SecondClass.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @title: ResponseStatus
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/9/22 14:14
 **/
@Getter
@AllArgsConstructor
public enum ResponseStatus {

    //通用响应
    SUCCESS("200","SUCCESS"),
    FAIL("400","FAIL"),
    ERROR("500","ERROR"),

    //用户登录响应
    USER_LOGIN_SUCCESS("1-1-200","登录成功！"),
    USER_LOGIN_FAIL("1-1-400","用户名或密码错误！"),

    //用户注册申请响应
    USER_REGISTER_REQUEST_SUCCESS("1-2-200","验证邮件已发送，请在5分钟内前往邮箱查看完成注册！"),
    USER_REGISTER_REQUEST_FAIL("1-2-400","用户名已被注册!"),
    USER_REGISTER_REQUEST_ERROR("1-2-500","验证邮件发送失败!"),

    //用户注册验证响应
    USER_REGISTER_SUCCESS("1-2-200","注册成功！"),
    USER_REGISTER_FAIL("1-2-400","验证邮件已过期，请重新注册！"),

    //用户密码修改
    USER_FORGET_SUCCESS("1-3-200","邮件已发送，请在5分钟内完成密码修改！"),
    USER_FORGET_FAIL("1-3-400","用户不存在，请查看用户名是否输入正确！"),
    USER_FORGET_ERROR("1-3-500","邮件发送失败！"),


    ;

    private final String responseStatus;

    private final String description;

}
