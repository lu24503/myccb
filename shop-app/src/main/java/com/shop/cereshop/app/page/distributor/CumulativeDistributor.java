/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.distributor;

import com.shop.cereshop.commons.domain.buyer.CereBuyerWithdrawal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 累计分销员
 */
@Data
@ApiModel(value = "CumulativeDistributor", description = "累计分销员")
public class CumulativeDistributor {

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
     * 累计订单数
     */
    @ApiModelProperty(value = "累计订单数")
    private Integer orders;

    /**
     * 累计消费金额
     */
    @ApiModelProperty(value = "累计消费金额")
    private BigDecimal price;

    /**
     * 是否展开,默认false
     */
    @ApiModelProperty(value = "是否展开,默认false")
    private boolean ifOpen=false;
}
