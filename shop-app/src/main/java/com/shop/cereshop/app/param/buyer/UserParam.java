/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.buyer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户请求
 */
@Data
@ApiModel(value = "UserParam", description = "用户请求")
public class UserParam {

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
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码")
    private String newPassword;
}
