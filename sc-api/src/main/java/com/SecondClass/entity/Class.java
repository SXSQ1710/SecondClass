package com.SecondClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (TClass)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Class implements Serializable {
    private static final long serialVersionUID = 469348194103564852L;
    /**
     * 班级id
     */
    @TableId(value = "cid",type= IdType.AUTO)
    private Long cid;
    /**
     * 班级名称
     */
    private String cname;
    /**
     * 年级
     */
    private Integer grade;
    /**
     * 专业
     */
    private String major;
    /**
     * 学院
     */
    private String college;
    /**
     * 校区
     */
    private String campus;




}

