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
 * 收货地址请求参数
 */
@Data
@ApiModel(value = "ReceiveParam", description = "收货地址请求参数")
public class ReceiveParam{

    /**
     * 收货地址id
     */
    @ApiModelProperty(value = "收货地址id")
    private Long receiveId;
}
