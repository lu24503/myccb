/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺生成海报的请求
 */
@Data
public class ShopPosterParam {

    /**
     * 推广类型 1-推广店铺 2-邀请下级
     */
    @ApiModelProperty(value = "推广类型 1-推广店铺 2-邀请下级")
    private Integer type;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 访问终端 1-APP 2-微信小程序 3-H5 4-支付宝小程序
     */
    @ApiModelProperty(value = "访问终端 1-APP 2-微信小程序 3-H5 4-支付宝小程序")
    private Integer terminal;

    /**
     * 分销员id
     */
    @ApiModelProperty(value = "分销员id")
    private Long distributorId;
}
