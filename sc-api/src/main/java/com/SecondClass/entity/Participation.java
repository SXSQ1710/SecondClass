package com.SecondClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * (TPaticipation)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:02
 */
@Data
public class Participation implements Serializable {
    private static final long serialVersionUID = -15005800947646660L;
    /**
     * 参加活动id
     */
    @TableId(value = "pid",type= IdType.AUTO)
    private String pid;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 活动id
     */
    private Long aid;
    /**
     * 参与状态 1：已报名 2：签到 3：签退 4：时长已发放
     */
    @JsonProperty(value = "participate_status")
    private Integer participateStatus;


}

