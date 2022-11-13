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
 * 查询自己报名参加的所有活动 包括活动参与状态、活动名字、活动图片等
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R_Activity_Participation {

    /**
     * 用户id
     */
    private String uid;
    /**
     * 活动id
     */
    private Long aid;
    /**
     * 参与状态 1：已报名 2：签到 3：签退 4：时长有效，允许发放 5：时长无效，拒绝发放
     */
    @JsonProperty(value = "participate_status")
    private Integer participateStatus;

    /**
     * 活动id
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
    @JsonProperty(value = "a_oid")
    private Long aOid;
    /**
     * 活动管理员（即活动申请人）
     */
    @JsonProperty(value = "a_uid")
    private Long aUid;
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
}
