/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.coupon;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 优惠券跳转商品列表请求
 */
@Data
@ApiModel(value = "ActivityParam", description = "优惠券跳转商品列表请求")
public class ActivityParam extends PageParam {

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id")
    private Long activityId;

    /**
     * 店铺优惠券id
     */
    @ApiModelProperty(value = "店铺优惠券id")
    private Long shopCouponId;
}
