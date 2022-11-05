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

    public void setHash(String key, String id, Object value){
        stringRedisTemplate.opsForHash().put(key, id, JSONUtil.toJsonStr(value));
    }

    public <R,ID> R queryForHash (String key, ID id, Class<R> type, Long time, TimeUnit unit, Function<ID, R> dbFallback){
        String json = (String) stringRedisTemplate.opsForHash().get(key, id);
        boolean isNotNull = StrUtil.isNotBlank(json);
        if (isNotNull){
            return  JSONUtil.toBean(json, type);
        }
//        if (json != null){
//            return null;
//        }

        R r = dbFallback.apply(id);

        if (r == null){
            stringRedisTemplate.opsForHash().put(key,id, "");
            return null;
        }

        setHash(key, id.toString(), r);
        return r;
    }

    public <R,ID> R queryForValue(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time, TimeUnit unit){
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        boolean notBlank = StrUtil.isNotBlank(json);
        if (notBlank){
            return JSONUtil.toBean(json, type);
        }
        if (json != null){
            return null;
        }

        R r = dbFallback.apply(id);

        if (r == null){
            stringRedisTemplate.opsForValue().set(key, "", 5, TimeUnit.MINUTES);
            return null;
        }

        setValue(key, r, time, unit);
        return r;
    }
}
