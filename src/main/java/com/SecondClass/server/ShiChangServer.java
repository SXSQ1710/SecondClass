package com.SecondClass.server;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import com.SecondClass.entity.ShiChang;
import com.SecondClass.mapper.ShiChangMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @title: ShiChangServer
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/10/27 17:31
 **/
@Slf4j
@Service
public class ShiChangServer {

    @Resource
    ShiChangMapper shiChangMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    public Response test(){
        //测试数据库
        List<ShiChang> shiChangs = shiChangMapper.selectList(null);
        Map<Long, List<ShiChang>> collect = shiChangs.stream().collect(Collectors.groupingBy(ShiChang::getSid));
        //测试redis
        for (ShiChang s :shiChangs){
            stringRedisTemplate.opsForHash().put("secondclass:shichang",s.getSid().toString(),s.getShichangName());
        }
        return Response.success(ResponseStatus.SUCCESS,collect);
    }
}
