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
 * 获取验证码请求实体
 */
@Data
@ApiModel(value = "GetCodeParam", description = "获取验证码参数实体")
public class BuyerGetCodeParam {

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "图形验证码")
    private String code;
}
