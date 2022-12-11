package com.SecondClass.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (TShichangApplication)实体类
 *
 * @author makejava
 * @since 2022-12-11 22:55:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_shichang_application")
public class ShichangApplication implements Serializable {
    private static final long serialVersionUID = -47919348507137167L;
    /**
     * 活动时长发放申请表id
     */
    private Long shiAppId;
    /**
     * 申请人id
     */
    private Long uid;
    /**
     * 时长发放的活动id
     */
    private Long aid;
    /**
     * 申请说明
     */
    private String shiAppDescription;
    /**
     * 申请状态 1：申请中 2：通过 0：拒绝
     */
    private Integer shiAppStatus;
    /**
     * 审核理由
     */
    private String shiAppExplain;

}

