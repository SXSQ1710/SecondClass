package com.SecondClass.entity.R_entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: R_Shi_App
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/12/12 13:27
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R_Shi_App {
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
