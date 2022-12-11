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
 * @title: R_AAppList
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/12/6 17:55
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R_AAppList {
    /**
     * 活动名字
     */
    private String aname;
    /**
     * 活动描述
     */
    private String adescription;
    /**
     * 报名开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty(value = "a_register_open")
    private Date aRegisterOpen;
    /**
     * 报名结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty(value = "a_register_close")
    private Date aRegisterClose;
    /**
     * 报名限制人数
     */
    @JsonProperty(value = "a_limitted_number")
    private Integer aLimittedNumber;
    /**
     * 举办单位
     */
    private String oname;
    /**
     * 活动管理员（即活动申请人）
     */
    private String uname;
    /**
     * 举办开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty(value = "a_hold_start")
    private Date aHoldStart;
    /**
     * 举办结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty(value = "a_hold_end")
    private Date aHoldEnd;
    /**
     * 活动图片路径
     */
    private String apic;
    /**
     * 活动时长数量
     */
    @JsonProperty(value = "a_shichang_num")
    private Integer aShichangNum;
    /**
     * 活动时长类型
     */
    @JsonProperty(value = "A_shichang_type")
    private Long aShichangType;
    /**
     * 活动地点
     */
    @JsonProperty(value = "a_address")
    private String aAddress;
    /**
     * 申请附件路径
     */
    private String aAppAttachment;
    /**
     * 申请状态 1：申请中 2：通过 0：拒绝
     */
    private Integer aAppStatus;

    private String aAppExplain;
}
