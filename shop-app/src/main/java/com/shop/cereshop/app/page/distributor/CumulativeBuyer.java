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
 * 累计客户
 */
@Data
@ApiModel(value = "CumulativeBuyer", description = "累计客户")
public class CumulativeBuyer {

    /**
     * 客户姓名
     */
    @ApiModelProperty(value = "客户姓名")
    private String customerName;

    /**
     * 客户手机号
     */
    @ApiModelProperty(value = "客户手机号")
    private String customerPhone;

    /**
     * 下单数
     */
    @ApiModelProperty(value = "下单数")
    private Integer orders;

    /**
     * 消费金额
     */
    @ApiModelProperty(value = "消费金额")
    private BigDecimal price;

    /**
     * 是否展开,默认false
     */
    @ApiModelProperty(value = "是否展开,默认false")
    private boolean ifOpen=false;
}
