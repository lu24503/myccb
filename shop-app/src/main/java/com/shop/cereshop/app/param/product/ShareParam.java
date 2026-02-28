/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分享商品请求
 */
@Data
@ApiModel(value = "ShareParam", description = "分享商品请求")
public class ShareParam {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 分销员id
     */
    @ApiModelProperty(value = "分销员id")
    private Long distributorId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;
}
