package com.SecondClass.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (TClass)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
public class Class implements Serializable {
    private static final long serialVersionUID = 469348194103564852L;
    /**
     * 班级id
     */
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

