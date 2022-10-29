package com.SecondClass.server;

import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import com.SecondClass.entity.ShiChang;
import com.SecondClass.entity.ShiChangType;
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
public class ShiChangServer {

    @Resource
    ShiChangTypeMapper shiChangTypeMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    public Response test(){
        //测试数据库
        List<ShiChangType> shiChangs = shiChangTypeMapper.selectList(null);
        Map<Long, List<ShiChangType>> collect = shiChangs.stream().collect(Collectors.groupingBy(ShiChangType::getSid));
        //测试redis
        for (ShiChangType s :shiChangs){
            stringRedisTemplate.opsForHash().put("secondclass:shichang",s.getSid().toString(),s.getShichangName());
        }
        return Response.success(ResponseStatus.SUCCESS,collect);
    }
}
