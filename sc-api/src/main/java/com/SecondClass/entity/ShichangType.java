package com.SecondClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (ShichangType)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_shichang_type")
public class ShichangType implements Serializable {
    private static final long serialVersionUID = 110615786567587556L;
    /**
     * 时长id
     */
    @TableId(value = "sid",type= IdType.AUTO)
    private Long sid;
    /**
     * 时长类型
     */
    private String shichangName;


}

