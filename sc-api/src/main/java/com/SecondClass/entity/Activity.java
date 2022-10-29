package com.SecondClass.entity;

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

    private String aAddress;

}

