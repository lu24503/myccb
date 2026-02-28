/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 结算优惠券请求
 */
@Data
@ApiModel(value = "OrderCouponParam", description = "结算优惠券请求")
public class OrderCouponParam {

    /**
     * 商品数据
     */
    @ApiModelProperty(value = "商品数据")
    private List<ProductCouponParam> params;
}
