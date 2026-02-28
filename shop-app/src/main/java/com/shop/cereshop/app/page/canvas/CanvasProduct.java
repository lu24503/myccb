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
 * 画布商品数据
 */
@Data
@ApiModel(value = "CanvasProduct", description = "画布商品数据")
public class CanvasProduct {

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
     * 店铺图片
     */
    @ApiModelProperty(value = "店铺图片")
    private String shopLogo;

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
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;

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

    /**
     * 销量
     */
    @ApiModelProperty(value = "销量")
    private Integer number;

    /**
     * 活动类型
     */
    @ApiModelProperty(value = "活动类型")
    private Integer activityType;

    /**
     * 付款人数
     */
    @ApiModelProperty(value = "付款人数")
    private Integer users;

    /**
     * 店铺拼团活动id
     */
    @ApiModelProperty(value = "店铺拼团活动id")
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
     * 是否开启预热状态 1-是 0-否
     */
    @ApiModelProperty(value = "是否开启预热状态 1-是 0-否")
    private Integer ifEnable;

    /**
     * 预热时间
     */
    @ApiModelProperty(value = "预热时间")
    private Integer enableTime;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    private String startTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    private String endTime;

    /**
     * 活动状态 0-未开始 1-进行中 2-已结束
     */
    @ApiModelProperty(value = "活动状态 0-未开始 1-进行中 2-已结束")
    private Integer state;

    /**
     * 活动倒计时
     */
    @ApiModelProperty(value = "活动倒计时")
    private long time;
}
