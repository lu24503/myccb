/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.order;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取订单列表请求
 */
@Data
@ApiModel(value = "OrderGetAllParam", description = "获取订单列表请求")
public class OrderGetByIdParam extends PageParam {

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 消息id
     */
    @ApiModelProperty(value = "消息id")
    private Long noticeId;

    /**
     * 订单类型 1-父订单 2-子订单
     */
    @ApiModelProperty(value = "订单类型 1-父订单 2-子订单")
    private Integer type = 2;
}
