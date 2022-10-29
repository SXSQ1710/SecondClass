package com.SecondClass.mapper;


import com.SecondClass.entity.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

}
