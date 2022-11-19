package com.SecondClass.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: MemberView
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/20 1:20
 **/
@Data
@TableName("t_member")
public class MemberView implements Serializable {
    private static final long serialVersionUID = 986772875698776276L;

    private Long cid;
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
    private Long phone;

    /**
     * 所属组织（为空时代表没有加入组织）
     */
    private String oid;
    /**
     * 用户姓名
     */
    private String uname;

    /**
     * 用户id
     */
    private Long uid;
    /**
     * 职位
     */
    private String position;
}
