/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.canvas;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 画布优惠券明细数据
 */
@Data
@ApiModel(value = "CanvasCouponDetail", description = "画布优惠券明细数据")
public class CanvasCouponDetail {

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    /**
     * 满多少元
     */
    @ApiModelProperty(value = "满多少元")
    private BigDecimal fullMoney;

    /**
     * 减多少元
     */
    @ApiModelProperty(value = "减多少元")
    private BigDecimal reduceMoney;
}
