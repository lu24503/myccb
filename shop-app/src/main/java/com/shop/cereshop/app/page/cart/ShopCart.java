/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 购物车数据
 */
@Data
@ApiModel(value = "ShopCart", description = "购物车数据")
public class ShopCart {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 选中状态 1-选中 0-未选中
     */
    @ApiModelProperty(value = "选中状态 1-选中 0-未选中")
    private Integer selected;

    /**
     * 商品数据
     */
    @ApiModelProperty(value = "商品数据")
    private List<CartSku> skus;
}
