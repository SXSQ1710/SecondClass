package com.SecondClass.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * (ShichangType)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
public class ShiChangType implements Serializable {
    private static final long serialVersionUID = 110615786567587556L;
    /**
     * 时长id
     */
    @Id
    private Long sid;
    /**
     * 时长类型
     */
    private String shichangName;


}

