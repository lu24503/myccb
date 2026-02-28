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
 * 首页推荐店铺数据
 */
@Data
@ApiModel(value = "RecommendShop", description = "首页推荐店铺数据")
public class RecommendShop {

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
     * 销量最高商品id
     */
    @ApiModelProperty(value = "销量最高商品id")
    private Long productId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 销量最高商品名称
     */
    @ApiModelProperty(value = "销量最高商品名称")
    private String productName;

    /**
     * 商品原价
     */
    @ApiModelProperty(value = "商品原价")
    private BigDecimal originalPrice;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价")
    private BigDecimal price;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;

    /**
     * 付款人数
     */
    @ApiModelProperty(value = "付款人数")
    private Integer users;

    /**
     * 活动限制总数
     */
    @ApiModelProperty(value = "活动限制总数")
    private Integer total;

    /**
     * 所属活动 0-常规商品 1-拼团活动 2-秒杀活动 3-限时折扣活动 4-平台秒杀 5-平台折扣 6-定价捆绑 7-组合捆绑 8-场景营销 9-会员价
     */
    @ApiModelProperty(value = "所属活动 0-常规商品 1-拼团活动 2-秒杀活动 3-限时折扣活动 4-平台秒杀 5-平台折扣 6-定价捆绑 7-组合捆绑 8-场景营销 9-会员价")
    private Integer activityType;
}
