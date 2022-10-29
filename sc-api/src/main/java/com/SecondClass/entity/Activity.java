package com.SecondClass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * (TActivity)实体类
 *
 * @author makejava
 * @since 2022-10-29 18:48:25
 */
@Data
public class Activity implements Serializable {
    private static final long serialVersionUID = 237752534655381205L;
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
    private Date aRegisterOpen;
    /**
     * 报名结束时间
     */
    private Date aRegisterClose;
    /**
     * 报名限制人数
     */

    private Integer aLimittedNumber;
    /**
     * 举办单位
     */
    private Long aOid;
    /**
     * 举办开始时间
     */
    private Date aHoldStart;
    /**
     * 举办结束时间
     */
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
    private Integer aShichangNum;
    /**
     * 活动时长类型
     */
    private Long aShichangType;


}

