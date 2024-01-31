package com.SecondClass.entity.R_entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @title: R_GetAllActivity
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/10/4 17:49
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R_GetAllActivity {
    private static final long serialVersionUID = -56724925718894306L;
    /**
     * 活动id
     */
    @TableId(value = "aid", type = IdType.AUTO)
    private Long aid;
    /**
     * 活动名字
     */
    private String aname;
    /**
     * 活动描述
     */
    private String adescription;

    /**
     * 活动地点
     */
    @JsonProperty(value = "a_address")
    private String aAddress;

    /**
     * 报名开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "a_register_open")
    private Date aRegisterOpen;

    /**
     * 报名结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "a_register_close")
    private Date aRegisterClose;

    /**
     * 报名限制人数
     */
    @JsonProperty(value = "a_limited_number")
    private Integer aLimitedNumber;

    /**
     * 举办开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "a_hold_start")
    private Date aHoldStart;
    /**
     * 举办结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "a_hold_end")
    private Date aHoldEnd;
    /**
     * 活动状态
     * 1:活动审核中
     * 2:活动审核通过
     */
    private Integer astatus;
    /**
     * 活动图片路径
     */
    private String apic;
    /**
     * 活动时长数量
     */
    private Integer aShichangNum;

    private String shichangName;

    private String oname;

    private String uname;

}
