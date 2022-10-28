package com.SecondClass.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @title: shichang
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/10/27 17:25
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShiChang implements Serializable {
    private static final long serialVersionUID = -59994883657983854L;
    /**
     * 时长id
     */
    private Long sid;
    /**
     * 时长类型
     */
    private String shichangName;


}
