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
 * 手机号合并微信请求
 */
@Data
@ApiModel(value = "UpdateWxPhoneParam", description = "手机号合并微信请求")
public class UpdateWxPhoneParam {

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String verificationCode;

    /**
     * 微信openid
     */
    @ApiModelProperty(value = "微信openid")
    private String wechatOpenId;

    /**
     * 绑定手机号
     */
    @ApiModelProperty(value = "绑定手机号")
    private String phone;

    /**
     * 微信头像
     */
    @ApiModelProperty(value = "微信头像")
    private String headImage;

    /**
     * ip
     */
    private String ip;

    /**
     * 渠道编码
     */
    private String channelCode;
}
