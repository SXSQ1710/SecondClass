package com.SecondClass.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (Shichang)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
public class Shichang implements Serializable {
    private static final long serialVersionUID = 826449020332591856L;
    /**
     * 时长id
     */
    private Long id;
    /**
     * 学生id
     */
    private Long uid;
    /**
     * 时长类型id
     */
    private Long sid;
    /**
     * 时长数量
     */
    private Integer snum;
    /**
     * 获得学年
     */
    private Date acquireTime;


}

