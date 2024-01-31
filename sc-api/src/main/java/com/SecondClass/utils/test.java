package com.SecondClass.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.SecondClass.entity.User;
import com.SecondClass.mapper.SemesterMapper;
import com.alibaba.druid.sql.visitor.functions.Char;
import io.lettuce.core.RedisBusyException;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @title: test
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/11/12 16:13
 **/
@Slf4j
public class test {
    public static void main(String[] args) {
        String s = "AB";
        int numRows = 1;
        int index = 0;
        boolean up = true;

        ArrayList<ArrayList<Character>> chars = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            chars.add(new ArrayList<Character>());
        }

        int j = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (j == numRows-1){
                up = false;
            }else if(j == 0){
                up = true;
            }

            if (up){
                j++;
                chars.get(j).add(c);
            }else {
                j = j > 0? j-1 : j;
                chars.get(j).add(c);
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < chars.size(); i++) {
            chars.get(i).forEach((element)->stringBuffer.append(element));
        }

        System.out.println(stringBuffer.toString());
    }


}

