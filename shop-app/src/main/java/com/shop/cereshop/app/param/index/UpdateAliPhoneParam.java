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
 * 手机号合并支付宝请求
 */
@Data
@ApiModel(value = "UpdateAliPhoneParam", description = "手机号合并支付宝请求")
public class UpdateAliPhoneParam {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long buyerUserId;

    /**
     * 绑定手机号
     */
    @ApiModelProperty(value = "绑定手机号")
    private String phone;

    /**
     * 手机号是否加密
     */
    @ApiModelProperty(value = "手机号是否加密")
    private Boolean encrypted;

    /**
     * ip
     */
    private String ip;
}
