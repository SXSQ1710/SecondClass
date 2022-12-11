package com.SecondClass.entity;


import lombok.Data;

/**
 * @title: RedisKeyName
 * @Author SXSQ
 * @Description redis的key
 * @Date 2022/11/3 22:06
 **/
@Data
public class RedisKeyName {
    public final static String ACTIVITY = "secondclass:activity:activity:";

    public final static String ACTIVITY_GET_SIGN_IN = "secondclass:activity:signIn:";

    public final static String ACTIVITY_GET_SIGN_OFF = "secondclass:activity:signOff:";

    public final static String ACTIVITY_APPLICATION = "secondclass:activity:applyActivity:";

    public final static String SECOND_KILL_STOCK = "seckill:stock:";

    public final static String SECOND_KILL_PARTICIPATION = "seckill:participation:";

    public final static String MANAGE_CLASS = "secondclass:manage:class:";

    public final static String MANAGE_USER = "secondclass:manage:user:";

    public final static String MANAGE_ORGANIZATION = "secondclass:manage:organization:oinfo:";

    public final static String MANAGE_ORGANIZATION_LEVEL = "secondclass:manage:organization:permissions:";

    public final static String MANAGE_ORGANIZATION_MEMBER = "secondclass:manage:omember:";

    public final static String SELF_APPLICATION = "secondclass:shichang:selfApplication:";

    public final static String SHICHANG_TYPE = "secondclass:shichang:shiChangType:";

    public final static String SHICHANG_INFO = "secondclass:shichang:userShiChangInfo:";

    public final static String SHICHANG_SEMESTER = "secondclass:semester:";

}
