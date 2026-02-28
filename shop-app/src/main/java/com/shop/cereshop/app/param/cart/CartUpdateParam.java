/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改购物车商品请求
 */
@Data
@ApiModel(value = "CartUpdateParam", description = "修改购物车商品请求")
public class CartUpdateParam {

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer number;
}
