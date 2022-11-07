package com.SecondClass.entity;


import lombok.Data;

/**
 * @title: RedisKeyName
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/11/3 22:06
 **/
@Data
public class RedisKeyName {
    public final static String ACTIVITY = "secondclass:activity:activity:";

    public final static String ACTIVITY_GET_SIGN_IN = "secondclass:activity:signIn:";

    public final static String ACTIVITY_GET_SIGN_OFF = "secondclass:activity:signOff:";

    public final static String ACTIVITY_APPLICATION = "secondclass:activity:applyActivity:";
}
