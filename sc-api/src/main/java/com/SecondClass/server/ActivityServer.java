package com.SecondClass.server;

import com.SecondClass.mapper.ActivityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ActivityServer {
    @Resource
    ActivityMapper activityMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;


}
