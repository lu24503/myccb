/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 再次购买请求
 */
@Data
@ApiModel(value = "BuyerAgainParam", description = "再次购买请求")
public class BuyerAgainParam {

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;
}
