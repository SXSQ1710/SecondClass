package com.SecondClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName: OrganizationApply
 * @Description //TODO
 * @Author jiang
 * @Date 2022/11/12 23:06
 **/
@Data
@TableName("t_organization_apply")
public class OrganizationApply {
    @TableId(type= IdType.AUTO)
    private Long oAppId;
    private Long uid;
    private Long oid;
    /**
     * oAppStatus 审核状态
     * 0：待审核
     * 2：已通过
     * 1：已拒绝
     */
    private String oAppStatus;

}
