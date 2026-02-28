/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.distributor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 累计奖励
 */
@Data
@ApiModel(value = "CumulativeReward", description = "累计奖励")
public class CumulativeReward {

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderFormid;

    /**
     * 佣金
     */
    @ApiModelProperty(value = "佣金")
    private BigDecimal commission;

    /**
     * 商品数
     */
    @ApiModelProperty(value = "商品数")
    private Integer products;

    /**
     * 实付金额
     */
    @ApiModelProperty(value = "实付金额")
    private BigDecimal price;

    /**
     * 下单人
     */
    @ApiModelProperty(value = "下单人")
    private String customerName;

    /**
     * 状态 是否结算 1-是 0-否
     */
    @ApiModelProperty(value = "状态 是否结算 1-是 0-否")
    private Integer state;

    /**
     * 分销员名称
     */
    @ApiModelProperty(value = "分销员名称")
    private String distributorName;

    /**
     * 是否展开,默认false
     */
    @ApiModelProperty(value = "是否展开,默认false")
    private boolean ifOpen=false;
}
