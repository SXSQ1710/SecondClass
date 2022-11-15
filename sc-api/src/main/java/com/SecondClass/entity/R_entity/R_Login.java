package com.SecondClass.entity.R_entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: R_Login
 * @Author SXSQ
 * @Description 登录请求体
 * @Date 2022/11/15 14:31
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R_Login {
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 用户密码
     */
    private String upassword;
    /**
     * 登录角色
     */
    private Integer role;
}
