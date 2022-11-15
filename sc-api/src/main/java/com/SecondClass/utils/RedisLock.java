package com.SecondClass.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @title: RedisLock
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/11/10 20:54
 **/

public class RedisLock {

    private String name;
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "lock:";

    public boolean tryLock(long timeoutSec){

        long threadId = Thread.currentThread().getId();
        Boolean success = stringRedisTemplate.opsForValue()
                .setIfAbsent(KEY_PREFIX + name, threadId+"", timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    public void unLock(){
        stringRedisTemplate.delete(KEY_PREFIX + name);
    }
}
