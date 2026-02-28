/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 领取店铺优惠券请求
 */
@Data
@ApiModel(value = "ShopCouponParam", description = "领取店铺优惠券请求")
public class ShopCouponParam {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 店铺优惠券id
     */
    @ApiModelProperty(value = "店铺优惠券id")
    private Long shopCouponId;
}
