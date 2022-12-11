package com.SecondClass.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.SecondClass.mapper.SemesterMapper;
import io.lettuce.core.RedisBusyException;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Calendar;
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
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        String currXueqi = (year - 1) + "-" + year+" 1";
        if (month > 8 || month < 2) {
            currXueqi = year + "-" + (year + 1)+" 2";
        }
        System.out.println(currXueqi);

    }
}
