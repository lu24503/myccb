/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("支付有礼优惠券对象")
public class OrderPoliteCouponDTO {

    /**
     * 优惠券类型
     */
    @ApiModelProperty("优惠券类型")
    private Integer couponType;

    /**
     * 优惠券优惠价或折扣值
     */
    @ApiModelProperty("OrderPoliteCouponDTO")
    private BigDecimal discount;

}
