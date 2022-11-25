package com.SecondClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (Shichang)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shichang implements Serializable {
    private static final long serialVersionUID = 826449020332591856L;
    /**
     * 时长id
     */
    @TableId(value = "id",type= IdType.AUTO)
    private Long id;
    /**
     * 学生id
     */
    private Long uid;
    /**
     * 获得时长详情
     */
    private String shiChang;
}

