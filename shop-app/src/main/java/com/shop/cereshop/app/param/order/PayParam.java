/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付请求
 */
@Data
@ApiModel(value = "PayParam", description = "支付请求")
public class PayParam {

    /**
     * 拼单id
     */
    @ApiModelProperty(value = "拼单id")
    private Long collageId;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 订单类型 1-父订单 2-子订单
     */
    @ApiModelProperty(value = "订单类型 1-父订单 2-子订单")
    private Integer type;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal money;

    /**
     * 订单支付方式 1-微信 2-支付宝
     */
    @ApiModelProperty("订单支付方式 1-微信 2-支付宝")
    private Integer paymentMode;

    /**
     * 花呗分期数
     */
    @ApiModelProperty("花呗分期数")
    private Integer huabeiPeriod;
}
