package com.SecondClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @title: ActicityApplication
 * @Author SXSQ
 * @Description //TODO 活动申请表
 * @Date 2022-10-30 13:27:14
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityApplication implements Serializable {
    private static final long serialVersionUID = 339862784358061142L;
    /**
     * 申请活动表id（物理id）
     */
    @TableId(value = "a_app_id",type= IdType.AUTO)
    @JsonProperty("a_app_id")
    private Long aAppId;
    /**
     * 申请人id
     */
    private Long uid;
    /**
     * 申请类型
     */
    private Long aAppType;
    /**
     * 申请描述
     */
    private String aAppDescription;
    /**
     * 申请附件路径
     */
    private String aAppAttachment;
    /**
     * 申请状态 1：申请中 2：通过 0：拒绝
     */
    private Integer aAppStatus;

    private String aAppExplain;
}