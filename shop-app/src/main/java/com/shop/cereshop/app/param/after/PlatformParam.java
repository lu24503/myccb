/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.after;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 申请平台介入
 */
@Data
@ApiModel(value = "PlatformParam", description = "申请平台介入")
public class PlatformParam {

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 售后单id
     */
    @ApiModelProperty(value = "售后单id")
    private Long afterId;

    /**
     * 问题描述
     */
    @ApiModelProperty(value = "问题描述")
    private String reason;

    /**
     * 图片地址（多个以逗号隔开）
     */
    @ApiModelProperty(value = "图片地址（多个以逗号隔开）")
    private String image;
}
