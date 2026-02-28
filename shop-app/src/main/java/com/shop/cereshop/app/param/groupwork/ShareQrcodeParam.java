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
 * 获取二维码请求
 */
@Data
@ApiModel(value = "ShareParam", description = "获取二维码请求")
public class ShareQrcodeParam {

    /**
     * 拼单id
     */
    @ApiModelProperty(value = "拼单id")
    private Long collageId;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 类型 暂时不知道用途
     */
    @ApiModelProperty(value = "类型")
    private Integer type;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 拼团id
     */
    @ApiModelProperty(value = "拼团id")
    private Long shopGroupWorkId;

}
