package com.SecondClass.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (TSelfApplication)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
public class SelfApplication implements Serializable {
    private static final long serialVersionUID = -86325089626812261L;
    /**
     * 自主申报表id
     */
    private Long selfAppId;
    /**
     * 申请人id
     */
    private Long uid;
    /**
     * 申请类型
     */
    private Long selfAppType;
    /**
     * 申请时长数量
     */
    private Integer selfAppShiNum;
    /**
     * 申请描述
     */
    private String selfAppDescription;
    /**
     * 申请附件
     */
    private String selfAppAttachment;
    /**
     * 申请状态  1：申请中 2：通过 0：拒绝
     */
    private Integer selfAppStatu;


}

