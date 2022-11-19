package com.SecondClass.mapper;

import com.SecondClass.entity.R_entity.R_ShiChang;
import com.SecondClass.entity.Shichang;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface ShiChangMapper extends BaseMapper<Shichang> {

    @Select("select shichang_name ,sum(snum) as total_num from t_shichang_type,t_shichang where t_shichang_type.sid = t_shichang.sid and uid = #{uid} GROUP BY t_shichang.sid")
    List<R_ShiChang> queryAllGroupBySid(Long uid);

    @Select("select t_shichang_type.sid,shichang_name ,sum(snum) as total_num from t_shichang_type,t_shichang where uid = #{uid} and t_shichang_type.sid = t_shichang.sid and #{startTime} <= acquire_time <= #{startTime} group by t_shichang_type.sid")
    List<R_ShiChang> queryAllGroupBySidAndTime(Long uid, Date startTime,Date endTime);
}
