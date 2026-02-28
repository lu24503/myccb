/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.groupwork;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 拼团商品详情请求
 */
@Data
@ApiModel(value = "GroupWorkProductParam", description = "拼团商品详情请求")
public class GroupWorkProductParam {

    /**
     * 店铺拼团活动id
     */
    @ApiModelProperty(value = "店铺拼团活动id")
    private Long shopGroupWorkId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;
}
