/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 搜索商品数据
 */
@Data
@ApiModel(value = "Product", description = "搜索商品数据")
public class Product {

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
     * 店铺图标
     */
    @ApiModelProperty(value = "店铺图标")
    private String shopLogo;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
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
     * 付款人数
     */
    @ApiModelProperty(value = "付款人数")
    private Integer users;

    /**
     * 商品简介
     */
    @ApiModelProperty(value = "商品简介")
    private String productBrief;

    /**
     * 所属活动 0-常规商品 1-拼团活动 2-秒杀活动 3-限时折扣活动 4-平台秒杀 5-平台折扣 6-定价捆绑 7-组合捆绑 8-场景营销 9-会员价
     */
    @ApiModelProperty(value = "所属活动 0-常规商品 1-拼团活动 2-秒杀活动 3-限时折扣活动 4-平台秒杀 5-平台折扣 6-定价捆绑 7-组合捆绑 8-场景营销 9-会员价")
    private Integer activityType;

    /**
     * 已售件数
     */
    @ApiModelProperty(value = "已售件数")
    private Integer number;

    /**
     * 活动限制总数
     */
    @ApiModelProperty(value = "活动限制总数")
    private Integer total;
}
