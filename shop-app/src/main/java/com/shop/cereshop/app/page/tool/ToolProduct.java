/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.tool;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 营销活动商品数据
 */
@Data
@ApiModel(value = "ToolProduct", description = "搜索商品数据")
public class ToolProduct {

    /**
     * 拼团活动id
     */
    @ApiModelProperty(value = "拼团活动id")
    private Long shopGroupWorkId;

    /**
     * 秒杀活动id
     */
    @ApiModelProperty(value = "秒杀活动id")
    private Long shopSeckillId;

    /**
     * 限时折扣活动id
     */
    @ApiModelProperty(value = "限时折扣活动id")
    private Long shopDiscountId;

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
     * 活动价格
     */
    @ApiModelProperty(value = "活动价格")
    private BigDecimal price;

    /**
     * 限时折扣/直降多少元
     */
    @ApiModelProperty(value = "限时折扣")
    private BigDecimal discount;

    /**
     * 活动商品限制数量
     */
    @ApiModelProperty(value = "活动商品限制数量")
    private Integer limitNumber;

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
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private Integer stockNumber;

    /**
     * 限量库存数量
     */
    @ApiModelProperty(value = "限量库存数量")
    private Integer limitStockNumber;

    /**
     * 已拼人数
     */
    @ApiModelProperty(value = "已拼人数")
    private Integer workUsers;

    /**
     * 活动类型
     */
    @ApiModelProperty(value = "活动类型")
    private Integer activityType;
}
