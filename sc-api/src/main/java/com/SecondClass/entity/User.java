package com.SecondClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * (TUser)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -32406263421502971L;
    /**
     * 用户id
     */
    @TableId(value = "uid",type= IdType.AUTO)
    private Long uid;
    /**
     * 用户密码
     */
    private String upassword;
    /**
     * 联系方式
     */
    private Long phone;
    /**
     * 班级id
     */
    private Long cid;
    /**
     * 所属组织（为空时代表没有加入组织）
     */
    private Long oid;
    /**
     * 用户姓名
     */
    private String uname;




}

