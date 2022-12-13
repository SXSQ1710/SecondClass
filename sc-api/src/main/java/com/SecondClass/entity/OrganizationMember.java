package com.SecondClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * (TOganizationMember)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
public class OrganizationMember implements Serializable {
    private static final long serialVersionUID = 845370379390237748L;
    /**
     * id
     */
    @TableId(value = "id",type= IdType.AUTO)
    private Long id;
    /**
     * 组织id
     */
    private Long oid;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 职位
     */
    private String position;


}

