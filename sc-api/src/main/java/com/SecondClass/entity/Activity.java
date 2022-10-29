package com.SecondClass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.io.Serializable;

/**
 * (TActivity)实体类
 *
 * @author makejava
 * @since 2022-10-29 09:33:01
 */

@Data
public class Activity implements Serializable {
    private static final long serialVersionUID = -56724925718894306L;
    /**
     * 活动id
     */
    private Long aid;
    /**
     * 活动名字
     */
    private String aname;
    /**
     * 活动地点
     */
    private String adescription;
    /**
     * 报名开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date aRegisterOpen;
    /**
     * 报名结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date aRegisterClose;
    /**
     * 报名限制人数
     */

    @JsonProperty(value = "aLimittedNumber")
    private Integer aLimittedNumber;
    /**
     * 举办单位
     */
    @JsonProperty(value = "aOid")
    private Long aOid;
    /**
     * 举办开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date aHoldStart;
    /**
     * 举办结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date aHoldEnd;
    /**
     * 活动状态
     */
    private Integer astatus;
    /**
     * 活动图片路径
     */
    private String apic;
    /**
     * 活动时长数量
     */
    @JsonProperty(value = "aShichangNum")
    private Integer aShichangNum;
    /**
     * 活动时长类型
     */
    @JsonProperty(value = "aShichangType")
    private Long aShichangType;

    @JsonProperty(value = "aAddress")
    private String aAddress;

}

