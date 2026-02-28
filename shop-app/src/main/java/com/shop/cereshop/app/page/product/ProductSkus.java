/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.product;

import com.shop.cereshop.commons.domain.product.Sku;
import com.shop.cereshop.commons.domain.product.SkuName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品所有规格属性数据
 */
@Data
@ApiModel(value = "ProductSkus", description = "商品所有规格属性数据")
public class ProductSkus {

    /**
     * 规格属性名数组
     */
    @ApiModelProperty(value = "规格属性名数组")
    private List<SkuName> names;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 商品原价
     */
    @ApiModelProperty(value = "商品原价")
    private BigDecimal originalPrice;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价")
    private BigDecimal price;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;

    /**
     * 商品库存
     */
    @ApiModelProperty(value = "商品库存")
    private Integer stockNumber;

    /**
     * 商品重量
     */
    @ApiModelProperty(value = "商品重量")
    private BigDecimal weight;

    /**
     * SKU
     */
    @ApiModelProperty(value = "SKU")
    private String SKU;

    /**
     * 所有规格属性组合数据
     */
    @ApiModelProperty(value = "所有规格属性组合数据")
    private Map<String, Sku> map;

    /**
     * 是否需要物流
     */
    @ApiModelProperty(value = "是否需要物流")
    private Integer ifLogistics;

    /**
     * 是否支持花呗分期
     */
    @ApiModelProperty(value = "是否支持花呗分期 1-是 0-否")
    private Integer ifHuabei;

    /**
     * 活动数据
     */
    private SkuTool skuTool;
}
