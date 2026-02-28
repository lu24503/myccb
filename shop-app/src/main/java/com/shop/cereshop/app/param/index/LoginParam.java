/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录请求参数
 */
@Data
@ApiModel(value = "LoginParam", description = "登录请求参数")
public class LoginParam {

    /**
     * APP端类型 1-注册 2-登录
     */
    @ApiModelProperty(value = "APP端类型 1-注册 2-登录")
    private Integer type;

    /**
     * 微信code 或 支付宝auth_code
     */
    @ApiModelProperty(value = "微信code 或 支付宝auth_code")
    private String code;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String verificationCode;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 访问终端 0-未知 1-APP 2-微信小程序 3-H5 4-支付宝小程序 5-PC
     */
    @ApiModelProperty(value = "访问终端 0-未知 1-APP 2-微信小程序 3-H5 4-支付宝小程序 5-PC")
    private Integer terminal;

    /**
     * 操作系统 1-Android 2-IOS
     */
    @ApiModelProperty(value = "操作系统 1-Android 2-IOS")
    private Integer system;

    /**
     * 所在地区
     */
    @ApiModelProperty(value = "所在地区")
    private String city;

    /**
     * 微信头像
     */
    @ApiModelProperty(value = "微信头像")
    private String headImage;

    /**
     * 登录ip
     */
    private String ip;

    /**
     * 渠道id
     */
    private String channelCode;
}
