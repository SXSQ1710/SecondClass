package com.SecondClass.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @ClassName: OrganizationApply
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/12 23:06
 **/
@TableName("t_organization_apply")
public class OrganizationApply {
    @TableId
    private Long oAppId;
    private Long uid;
    private Long oid;
    private String oAppReason;
    private String oAppStatus;
}
