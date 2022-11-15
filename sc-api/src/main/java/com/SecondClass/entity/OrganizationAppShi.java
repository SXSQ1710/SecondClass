package com.SecondClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (OganizationAppShi)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationAppShi implements Serializable {
    private static final long serialVersionUID = 573628772029508401L;
    @TableId(value = "shi_app_id",type= IdType.AUTO)
    private Long shiAppId;
    
    private Long uid;
    
    private Long sid;
    
    private String shiAppDescription;
    
    private String shiAppAttachment;
    
    private Integer shiAppStatus;



}

