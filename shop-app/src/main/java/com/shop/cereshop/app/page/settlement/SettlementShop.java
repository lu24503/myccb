/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.settlement;

import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.product.ProductCoupon;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 结算返回店铺数据
 */
@Data
@ApiModel(value = "SettlementShop", description = "结算返回店铺数据")
public class SettlementShop {

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
     * 店铺地址
     */
    @ApiModelProperty(value = "店铺地址")
    private String shopAdress;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺logo")
    private String shopLogo;

    /**
     * 商品数据
     */
    @ApiModelProperty(value = "商品数据")
    private List<CartSku> skus;

    /**
     * 配送方式信息
     */
    @ApiModelProperty(value = "配送方式信息")
    private Distribution distribution;

    /**
     * 商品总件数
     */
    @ApiModelProperty(value = "商品总件数")
    private Integer number;

    /**
     * 店铺总计
     */
    @ApiModelProperty(value = "店铺总计")
    private BigDecimal total;

    /**
     * 店铺优惠券数据
     */
    @ApiModelProperty(value = "店铺优惠券数据")
    private List<ProductCoupon> shopCoupons;

    /**
     * sku的优惠信息
     */
    @ApiModelProperty(value = "sku的优惠信息")
    private Map<Long, List<String>> skuDiscountInfoMap;

    /**
     * 定价捆绑sku优惠后的金额
     */
    @ApiModelProperty(value = "定价捆绑sku优惠后的金额")
    private BigDecimal priceAfterDiscount;

    /**
     * 收货地址与商家配送区域不匹配
     */
    @ApiModelProperty("收货地址与商家配送区域不匹配")
    private boolean receiveNotMatch;
}
