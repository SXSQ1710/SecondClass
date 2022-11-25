package com.SecondClass.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.lettuce.core.RedisBusyException;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
import java.util.List;

/**
 * @title: test
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/11/12 16:13
 **/
@Slf4j
public class test {
    public static void createConsumerGroup(RedisTemplate redisTemplate) {

    }

    public static void main(String[] args) {
        String json = "-";
        String[] split = json.split("-");
        System.out.println(split.length);
        System.out.println("-----");
        for (String s :split){
            System.out.println(s);
            System.out.println("----");
        }
    }
}
