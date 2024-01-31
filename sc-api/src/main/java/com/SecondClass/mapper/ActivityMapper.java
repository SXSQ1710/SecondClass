package com.SecondClass.mapper;


import com.SecondClass.entity.Activity;
import com.SecondClass.entity.R_entity.R_GetAllActivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {
    List<R_GetAllActivity> getAll(@Param("pageNo")Long pageNo, @Param("pageSize")Long pageSize);
}
