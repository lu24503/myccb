/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.settlement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 结算返回规格数据
 */
@Data
@ApiModel(value = "SettlementSku", description = "结算返回规格数据")
public class SettlementSku {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价")
    private BigDecimal productPrice;

    /**
     * 商品小计
     */
    @ApiModelProperty(value = "商品小计")
    private BigDecimal total;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;

    /**
     * 规格值拼接字符串
     */
    private String value;

    /**
     * 规格值数据
     */
    @ApiModelProperty(value = "规格值数据")
    private List<String> values;
}
