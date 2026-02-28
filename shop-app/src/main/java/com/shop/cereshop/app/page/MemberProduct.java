/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MemberProduct implements Serializable {

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
     * 店铺LOGO
     */
    @ApiModelProperty(value = "店铺LOGO")
    private String shopLogo;

    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private Long productId;

    /**
     * 优惠方式   1-折扣 2-指定价格
     */
    @ApiModelProperty(value = "优惠方式   1-折扣 2-指定价格")
    private Integer mode;

    /**
     * 会员等级id
     */
    @ApiModelProperty(value = "会员等级id")
    private Long memberLevelId;

    /**
     * 会员等级名称
     */
    @ApiModelProperty(value = "会员等级名称")
    private String memberLevelName;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**
     * 产品主图
     */
    @ApiModelProperty(value = "产品主图")
    private String image;

    /**
     * 商品原价
     */
    @ApiModelProperty(value = "商品原价")
    private BigDecimal originalPrice;

    /**
     * 商品会员价元/折扣
     */
    @ApiModelProperty(value = "商品会员价元/折扣")
    private BigDecimal discount;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价")
    private BigDecimal price;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存")
    private Integer stockNumber;

    /**
     * 销量
     */
    @ApiModelProperty(value = "销量")
    private Integer number;

    /**
     * 付款人数
     */
    @ApiModelProperty(value = "付款人数")
    private Integer users;
}
