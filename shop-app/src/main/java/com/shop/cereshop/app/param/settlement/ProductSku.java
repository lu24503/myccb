/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.settlement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 结算查询商品规格参数
 */
@Data
@ApiModel(value = "ProductSku", description = "结算查询商品规格参数")
public class ProductSku implements Serializable {

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer number;

    /**
     * 是否需要物流 1-是 0-否
     */
    @ApiModelProperty(value = "是否需要物流 1-是 0-否")
    private Integer ifLogistics;

    /**
     * 是否购物车选中 1-是 0-否
     */
    @ApiModelProperty(value = "是否购物车选中 1-是 0-否")
    private Integer selected;

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
     * 平台秒杀活动id
     */
    @ApiModelProperty(value = "平台秒杀活动id")
    private Long platformSeckillId;

    /**
     * 平台限时折扣活动id
     */
    @ApiModelProperty(value = "平台限时折扣活动id")
    private Long platformDiscountId;

    /**
     * 是否使用会员价
     */
    @ApiModelProperty(value = "是否使用会员价")
    private boolean useMember;

    /**
     * 定价捆绑id
     */
    @ApiModelProperty(value = "定价捆绑id")
    private Long priceId;

    /**
     * 组合捆绑id
     */
    @ApiModelProperty(value = "组合捆绑id")
    private Long composeId;

    /**
     * 场景营销id
     */
    @ApiModelProperty(value = "场景营销id")
    private Long sceneId;

    /**
     * 当前sku抵扣积分
     */
    @ApiModelProperty(value = "当前sku抵扣积分")
    private Integer useCredit;

    /**
     * 当前sku积分抵扣的金额
     */
    @ApiModelProperty(value = "当前sku积分抵扣的金额")
    private BigDecimal useCreditAmount;

    /**
     * 平台优惠券id 有值则代表该sku享受了平台优惠券
     */
    @ApiModelProperty(value = "平台优惠券id")
    private Long buyerCouponId;

    /**
     * 商家优惠券id 有值就代表该sku享受了商家优惠券
     */
    @ApiModelProperty(value = "商家优惠券id")
    private Long buyerShopCouponId;

    /**
     * 商品重量
     */
    @ApiModelProperty(value = "商品重量")
    private BigDecimal weight;
}
