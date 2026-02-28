/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 店铺优惠券数据
 */
@Data
@ApiModel(value = "ShopCoupon", description = "店铺优惠券数据")
public class ShopCoupon {

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    private Long shopCouponId;

    /**
     * 优惠券类型 1-满减券 2-折扣券
     */
    @ApiModelProperty(value = "优惠券类型 1-满减券 2-折扣券")
    private Integer couponType;

    /**
     * 使用门槛满多少元，无门槛为0
     */
    @ApiModelProperty(value = "使用门槛满多少元，无门槛为0")
    private BigDecimal threshold;

    /**
     * 优惠内容减多少元/打多少折
     */
    @ApiModelProperty(value = "优惠内容减多少元/打多少折")
    private BigDecimal couponContent;

    /**
     * 领取状态 0-已领取 1-已使用 2-未领取
     */
    @ApiModelProperty(value = "领取状态 0-已领取 1-已使用 2-未领取")
    private Integer state;

    /**
     * 限制领取次数
     */
    private Integer frequency;

    /**
     * 库存数量
     */
    private Integer stockNumber;
}
