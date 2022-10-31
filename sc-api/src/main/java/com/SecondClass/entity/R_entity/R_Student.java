package com.SecondClass.entity.R_entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R_Student {
    /**
     * 用户ID
     */
    private Long uid;

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
