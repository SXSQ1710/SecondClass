package com.SecondClass.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (TUser)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ColumnWidth(15)//注释在具体属性上,设置单独列。注释在类上,统一设置列宽
@HeadRowHeight(15)//设置表头行高
@ContentRowHeight(20)//统一设置数据行行高
public class User implements Serializable {
    private static final long serialVersionUID = -32406263421502971L;
    /**
     * 用户id
     */
    @TableId(value = "uid")
    @ExcelProperty(value = "学号", index = 0)
    private Long uid;
    /**
     * 用户密码
     */
    @ExcelProperty(value = "密码", index = 1)
    private String upassword;
    /**
     * 联系方式
     */
    @ExcelProperty(value = "联系方式", index = 2)
    private Long phone;
    /**
     * 班级id
     */
    @ExcelProperty(value = "班级", index = 3)
    private Long cid;
    /**
     * 所属组织（为空时代表没有加入组织）
     */
    @ExcelProperty(value = "所属组织", index = 4)
    private String oid;
    /**
     * 用户姓名
     */
    @ExcelProperty(value = "用户名", index = 5)
    private String uname;
    /**
     * 年级
     */
    @ExcelProperty(value = "年级", index = 6)
    private Integer grade;
}

