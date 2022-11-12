package com.SecondClass.utils;

import io.lettuce.core.RedisBusyException;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;

/**
 * @title: test
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/11/12 16:13
 **/
@Slf4j
public class test {
    public static void createConsumerGroup(RedisTemplate redisTemplate) {
        try {
            //redisTemplate.opsForStream().createGroup("my-stream", ReadOffset.from("0-0"), "my-group");
            redisTemplate.opsForStream().createGroup("stream.participation", "g1");
        } catch (RedisSystemException e) {
            if (e.getRootCause().getClass().equals(RedisBusyException.class)) {
                log.info("STREAM - Redis group already exists, skipping Redis group creation: my-group-2");
            } else if (e.getRootCause().getClass().equals(RedisCommandExecutionException.class)) {
                log.info("STREAM - Stream does not yet exist, creating empty stream: event-stream");
                // TODO: There has to be a better way to create a stream than this!?
                redisTemplate.opsForStream().add("event-stream", Collections.singletonMap("", ""));
                redisTemplate.opsForStream().createGroup("event-stream", "my-group-2");
            } else throw e;
        }
    }

    public static void main(String[] args) {
        RedisTemplate redisTemplate = new RedisTemplate();
        createConsumerGroup(redisTemplate);
    }
}
