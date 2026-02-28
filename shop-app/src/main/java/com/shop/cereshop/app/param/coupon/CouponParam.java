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
 * 领取优惠券请求
 */
@Data
@ApiModel(value = "CouponParam", description = "领取优惠券请求")
public class CouponParam {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 平台优惠券id
     */
    @ApiModelProperty(value = "平台优惠券id")
    private Long couponId;

    /**
     * 店铺优惠券id
     */
    @ApiModelProperty(value = "店铺优惠券id")
    private Long shopCouponId;

    /**
     * 是否采用积分兑换
     */
    @ApiModelProperty(value = "是否采用积分兑换")
    private Integer useCredit;

    /**
     * 来源 1-正常领取 2-场景营销发放 3-积分兑换 4-支付有礼
     */
    @ApiModelProperty(value = "来源 1-正常领取 2-场景营销发放 3-积分兑换 4-支付有礼 5-渠道券活动")
    private Integer source;

    @ApiModelProperty(value = "微信卡券code")
    private String couponCode;
}
