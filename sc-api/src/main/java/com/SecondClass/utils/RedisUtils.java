package com.SecondClass.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.SecondClass.entity.RedisKeyName;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @title: RedisUtils
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/11/4 16:55
 **/
@Slf4j
@Component
public class RedisUtils {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    public void setValue(String redisKeyName, Object value, Long time, TimeUnit unit){
        stringRedisTemplate.opsForValue().set(redisKeyName, JSONUtil.toJsonStr(value),time,unit);
    }

    public void setValue(String redisKeyName, Object value){
        stringRedisTemplate.opsForValue().set(redisKeyName, JSONUtil.toJsonStr(value));
    }

    public void setHash(String key, String id, Object value){
        stringRedisTemplate.opsForHash().put(key, id, JSONUtil.toJsonStr(value));
    }

    /**
     * redis的hash查询
     * @param key key前缀
     * @param id hashKey
     * @param type 查询的类
     * @param time 缓存穿透保护生成的空类过期时间
     * @param unit 过期时间单位
     * @param dbFallback 缓存未命中的sql查询函数
     * @param <R>  返回类型
     * @return R
     */
    public <R> R queryForHash (String key, String id, Class<R> type, Long time, TimeUnit unit, Function<String, R> dbFallback){
        //1.先查询redis缓存
        String json = (String) stringRedisTemplate.opsForHash().get(key, id);
        boolean isNotNull = StrUtil.isNotBlank(json);
        if (isNotNull){
            //1.1.redis缓存命中，返回结果
            return  JSONUtil.toBean(json, type);
        }
        //2.redis缓存未命中，查询数据库，查询数据库的代码通过函数传入
        R r = dbFallback.apply(id);
        //2.1.数据库未查询到结果，在缓存中创建id对应的空值，防止缓存穿透
        if (r == null){
            stringRedisTemplate.opsForHash().put(key,id, "");
            stringRedisTemplate.expire(key, 5L, TimeUnit.MINUTES);
            return null;
        }

        //2.2.将数据库查询结果存入缓存
        setHash(key, id, r);
        stringRedisTemplate.expire(key, time, unit);
        return r;
    }

    /**
     * 
     * @param keyPrefix
     * @param id hashKey
     * @param type 查询的类
     * @param time 缓存穿透保护生成的空类过期时间
     * @param unit 过期时间单位
     * @param dbFallback 缓存未命中的sql查询函数
     * @param <R>  返回类型
     * @return R
     */
    public <R> R queryForValue(String keyPrefix, String id, Class<R> type, Long time, TimeUnit unit, Function<String, R> dbFallback){
        //1.先查询redis缓存
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        boolean notBlank = StrUtil.isNotBlank(json);
        if (notBlank){
            //1.1.redis缓存命中，返回结果
            return JSONUtil.toBean(json, type);
        }
        if (json != null){
            return null;
        }
        //2.redis缓存未命中，查询数据库，查询数据库的代码通过函数传入
        R r = dbFallback.apply(id);
        //2.1.数据库未查询到结果，在缓存中创建id对应的空值，防止缓存穿透
        if (r == null){
            stringRedisTemplate.opsForValue().set(key, "", 5, TimeUnit.MINUTES);
            return null;
        }
        //2.2.将数据库查询结果存入缓存
        setValue(key, r, time, unit);
        return r;
    }
}
