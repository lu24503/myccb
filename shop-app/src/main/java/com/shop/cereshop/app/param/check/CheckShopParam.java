/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询是否申请请求
 */
@Data
@ApiModel(value = "CheckShopParam", description = "查询是否申请请求")
public class CheckShopParam {

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String shopPhone;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String code;
}
