/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.discount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 限时折扣商品详情请求
 */
@Data
@ApiModel(value = "DiscountlProductParam", description = "限时折扣商品详情请求")
public class DiscountlProductParam {

    /**
     * 限时折扣活动id
     */
    @ApiModelProperty(value = "限时折扣活动id")
    private Long shopDiscountId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;
}
