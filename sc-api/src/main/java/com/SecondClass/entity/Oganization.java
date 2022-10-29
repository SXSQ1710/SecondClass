package com.SecondClass.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (TOganization)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
public class Oganization implements Serializable {
    private static final long serialVersionUID = -87442678176810533L;
    /**
     * 组织id
     */
    private Long oid;
    /**
     * 组织名字
     */
    private String oname;
    /**
     * 负责人id
     */
    private Long uid;
    /**
     * 所属校区
     */
    private String campus;
    /**
     * 组织描述
     */
    private String odescription;
    /**
     * 上级单位
     */
    private String superiorOganization;


}

