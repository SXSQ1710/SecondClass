package com.SecondClass.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @title: ResponseStatus
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/9/22 14:14
 **/
@Getter
@AllArgsConstructor
public enum ResponseStatus {

    //通用响应
    SUCCESS("200","SUCCESS"),
    FAIL("400","FAIL"),
    ERROR("500","ERROR"),

    /** ResponseStatus说明：
     * 成功响应为：200
     * 失败响应为：400
     * 服务器错误响应为：500
     *
     * 1：用户登录相关响应
     * 2：活动申请相关响应
     * 3：
     * 4：
     * .....其他的直接根据想要填写
     *
     * 例：
     *   1-200：用户登录成功响应
     *   2-400：活动申请失败响应
     */

    //用户登录响应
    USER_LOGIN_SUCCESS("1-200","登录成功！"),
    USER_LOGIN_FAIL("1-400","用户名或密码错误！"),

    //活动相关响应
    ACTIVITY_APPLY_SUCCESS("2-200","活动申请成功，请等待审核结果！"),
    ACTIVITY_APPLY_FAIL("2-400","活动申请失败，请检查申请信息！"),
    ACTIVITY_APPLY_ERROR("2-500","服务器错误！"),

    //审核相关相应
    ADUIT_SUCCESS("3-200","审核操作成功！"),
    ADUIT_FAIL("3-400","审核操作失败！"),

    //申请查询相关相应
    ACTIVITY_APP_QUERY_SUCCESS("4-200","查询成功"),
    ACTIVITY_APP_QUERY_FAIL("4-400","没有相关数据"),

    //活动相关相应
    ACTIVITY_QUERY_SUCCESS("5-200","查询成功"),
    ACTIVITY_QUERY_FAIL("5-400","没有相关数据"),
    ACTIVITY_GET_SIGN_IN_SUCCESS("5-200","获取签到码成功"),
    ACTIVITY_GET_SIGN_IN_FAIL("5-400","获取签到码失败，请查看信息是否正确"),
    ACTIVITY_SIGN_IN_SUCCESS("5-200","签到成功"),
    ACTIVITY_SIGN_IN_FAIL("5-400","签到失败"),
    ACTIVITY_SIGN_OFF_SUCCESS("5-200","签退成功"),
    ACTIVITY_SIGN_OFF_FAIL("5-400","签退失败"),

    //报名相关响应
    REGISTER_ACTIVITY_SUCCESS("6-200","报名成功"),
    REGISTER_ACTIVITY_FAIL("6-400","报名失败");


    private final String responseStatus;

    private final String description;

}
