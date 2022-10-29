package com.SecondClass.server;

import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import com.SecondClass.entity.ShichangType;
import com.SecondClass.mapper.ShiChangTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class ShiChangTypeServer {

    @Resource
    ShiChangTypeMapper shiChangMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    public Response test(){
        //测试数据库
        List<ShichangType> shiChangs = shiChangMapper.selectList(null);
        Map<Long, List<ShichangType>> collect = shiChangs.stream().collect(Collectors.groupingBy(ShichangType::getSid));
        //测试redis
        for (ShichangType s :shiChangs){
            stringRedisTemplate.opsForHash().put("secondclass:shichang",s.getSid().toString(),s.getShichangName());
        }
        return Response.success(ResponseStatus.SUCCESS,collect);
    }
}
