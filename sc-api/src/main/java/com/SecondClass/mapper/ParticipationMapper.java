package com.SecondClass.mapper;

import com.SecondClass.entity.Participation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface ParticipationMapper extends BaseMapper<Participation> {
    @MapKey("uid")
    Map<String,Object> getAllParticipatedMember(@Param("aid") Long aid,  @Param("status") Integer status);

    @MapKey("cid")
    Map<String,Object> getClassParticipationData (@Param("aid") Long aid);

    void creatParticipationTemp(@Param("grade") Long grade);
}
