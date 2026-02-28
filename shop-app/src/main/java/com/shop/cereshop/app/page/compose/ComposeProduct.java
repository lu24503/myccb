/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.compose;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 组合商品明细
 * @author
 */
@Data
@ApiModel(value = "ComposeProduct", description = "组合商品明细")
public class ComposeProduct {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId")
    private Long skuId;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long shopId;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 售价区间
     */
    @ApiModelProperty(value = "售价区间")
    private String sectionPrice;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存")
    private Integer stockNumber;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价")
    private BigDecimal price;

    /**
     * 原价
     */
    @ApiModelProperty(value = "原价")
    private BigDecimal originalPrice;
}
