/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.distributor;

import com.shop.cereshop.commons.constant.IntegerEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分销订单数据
 */
@Data
@ApiModel(value = "DistributorOrder", description = "分销订单数据")
public class DistributorOrder {

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderFormid;

    /**
     * 分销员id
     */
    @ApiModelProperty(value = "分销员id")
    private Long distributorId;

    /**
     * 分销员名称
     */
    @ApiModelProperty(value = "分销员名称")
    private String distributorName;

    /**
     * 分销员手机号
     */
    @ApiModelProperty(value = "分销员手机号")
    private String distributorPhone;

    /**
     * 佣金
     */
    @ApiModelProperty(value = "佣金")
    private BigDecimal commission;

    /**
     * 是否结算 1-是 0-否
     */
    @ApiModelProperty(value = "是否结算 1-是 0-否")
    private Integer state;

    /**
     * 佣金类型 1-直接佣金 2-间接佣金
     */
    @ApiModelProperty(value = "佣金类型 1-直接佣金 2-间接佣金")
    private Integer type;

    /**
     * 交易时间
     */
    @ApiModelProperty(value = "交易时间")
    private String transactionTime;

    /**
     * 是否展开
     */
    @ApiModelProperty(value = "是否展开")
    private boolean ifOpen=false;

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
}
