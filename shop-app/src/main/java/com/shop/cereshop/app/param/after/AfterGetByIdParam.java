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
 * 售后详情请求
 */
@Data
@ApiModel(value = "AfterGetByIdParam", description = "售后详情请求")
public class AfterGetByIdParam {

    /**
     * 售后单id
     */
    @ApiModelProperty(value = "售后单id")
    private Long afterId;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

}
