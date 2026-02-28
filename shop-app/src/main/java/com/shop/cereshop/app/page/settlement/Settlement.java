/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.settlement;

import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.commons.domain.buyer.CereBuyerReceive;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 结算查询返回数据
 */
@Data
@ApiModel(value = "Settlement", description = "结算查询返回数据")
public class Settlement {

    /**
     * 收货地址信息
     */
    @ApiModelProperty(value = "收货地址信息")
    private CereBuyerReceive receive;

    /**
     * 商品明细数据
     */
    @ApiModelProperty(value = "商品明细数据")
    private List<SettlementShop> shops;

    /**
     * 置灰商品数据
     */
    @ApiModelProperty(value = "商品明细数据")
    private List<SettlementShop> invalidShops;

    /**
     * 平台优惠券数据
     */
    @ApiModelProperty(value = "平台优惠券数据")
    private List<ProductCoupon> coupons;

    /**
     * 花呗手续费支付方式 1-商户支付 2-用户支付
     */
    @ApiModelProperty(value = "花呗手续费支付方式 1-商户支付 2-用户支付")
    private Integer huabeiChargeType;

    /**
     * 花呗手续费比例列表 3期 6期 12期
     */
    @ApiModelProperty(value = "花呗手续费比例列表 3期 6期 12期")
    private List<Double> huabeiFeerateList;

    @ApiModelProperty("用户总积分")
    private Integer userTotalCredit;

    @ApiModelProperty("每个sku可以抵扣多少积分")
    private Map<Long, Integer> skuCreditMap;

    @ApiModelProperty("每单满足多少金额才能抵扣积分")
    private BigDecimal orderCreditThreshold;

    @ApiModelProperty("每单最多抵扣多少积分")
    private Integer creditDeductLimit;

}
