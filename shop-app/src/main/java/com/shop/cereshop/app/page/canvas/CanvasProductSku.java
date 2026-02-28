/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.canvas;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 画布商品sku数据
 */
@Data
@ApiModel(value = "CanvasProductSku", description = "画布商品sku数据")
public class CanvasProductSku {

    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private Long productId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 产品主图
     */
    @ApiModelProperty(value = "产品主图")
    private String image;

    /**
     * 商品原价
     */
    @ApiModelProperty(value = "商品原价")
    private BigDecimal originalPrice;

    /**
     * 商品售价
     */
    @ApiModelProperty(value = "商品售价")
    private BigDecimal price;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存")
    private Integer stockNumber;

}
