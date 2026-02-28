/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 校验商品库存
 */
@Data
@ApiModel(value = "CheckSku", description = "校验商品库存")
public class CheckSku {

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 商品规格id
     */
    @ApiModelProperty(value = "商品规格id")
    private Long skuId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 商品售价
     */
    @ApiModelProperty(value = "商品售价")
    private BigDecimal price;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private Integer stockNumber;

    /**
     * 是否允许超卖 1-是 0-否
     */
    @ApiModelProperty(value = "是否允许超卖 1-是 0-否")
    private Integer ifOversold;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer number;

    /**
     * 是否购物车选中 1-是 0-否
     */
    @ApiModelProperty(value = "是否购物车选中 1-是 0-否")
    private Integer selected;
}
