package com.shop.cereshop.app.param.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("优惠券领取状态")
public class CouponTakeState {

    @ApiModelProperty("优惠券id")
    private Long couponId;

    @ApiModelProperty("领取状态 0-已领取 1-已使用 2-已过期 3-未领取")
    private Integer state;

}
