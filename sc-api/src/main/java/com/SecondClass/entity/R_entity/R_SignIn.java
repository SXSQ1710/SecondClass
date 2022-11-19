package com.SecondClass.entity.R_entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: R_SignIn
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/11/3 22:15
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R_SignIn {
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 活动密钥
     */
    private String uuid;
    /**
     * 签到签退类型
     */
    private String type;
}
