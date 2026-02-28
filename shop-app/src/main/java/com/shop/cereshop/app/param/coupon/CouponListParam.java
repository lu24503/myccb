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
 * 优惠券列表请求
 */
@Data
@ApiModel(value = "CouponListParam", description = "优惠券列表请求")
public class CouponListParam {

    /**
     * 状态 0-已领取 1-已使用 2-已过期
     */
    @ApiModelProperty(value = "状态 0-已领取 1-已使用 2-已过期")
    private Integer state;
}
