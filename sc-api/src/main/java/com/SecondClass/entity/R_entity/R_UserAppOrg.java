package com.SecondClass.entity.R_entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: R_UserInfo
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/11/11 10:37
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R_UserAppOrg {
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 申请加入的组织id
     */
    private Long appOid;
    /**
     * 申请加入的组织名
     */
    private String appOname;
    /**
     * 用户姓名
     */
    private String uname;
    /**
     * 联系方式
     */
    private Long phone;

    /**
     * 班级名称
     */
    private String cname;
    /**
     * 年级
     */
    private Integer grade;
    /**
     * 专业
     */
    private String major;
    /**
     * 学院
     */
    private String college;
    /**
     * 校区
     */
    private String campus;
}
