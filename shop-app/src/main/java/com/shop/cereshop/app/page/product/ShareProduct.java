/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分享商品数据
 */
@Data
@ApiModel(value = "ShareProduct", description = "分享商品数据")
public class ShareProduct {

    /**
     * 客户姓名
     */
    @ApiModelProperty(value = "客户姓名")
    private String name;

    /**
     * 客户头像
     */
    @ApiModelProperty(value = "客户头像")
    private String headImage;

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
    private BigDecimal price;

    /**
     * 商品重量
     */
    @ApiModelProperty(value = "商品重量")
    private BigDecimal weight;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;

    /**
     * SKU
     */
    @ApiModelProperty(value = "SKU")
    private String SKU;

    /**
     * 规格值拼接字符串
     */
    private String value;

    /**
     * 规格值数据
     */
    @ApiModelProperty(value = "规格值数据")
    private List<String> values;

    /**
     * 小程序码
     */
    @ApiModelProperty(value = "小程序码")
    private String shareImage;

}
