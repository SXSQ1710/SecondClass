package com.SecondClass.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (OganizationAppShi)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
public class OganizationAppShi implements Serializable {
    private static final long serialVersionUID = 573628772029508401L;
    
    private Long shiAppId;
    
    private Long uid;
    
    private Long sid;
    
    private String shiAppDescription;
    
    private String shiAppAttachment;
    
    private Integer shiAppStatus;



}

