/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单数量")
public class OrderCountDTO {

    /**
     * 待付款订单数
     */
    @ApiModelProperty(value = "待付款订单数")
    private Integer waitPayOrderCount;

    /**
     * 待发货订单数
     */
    @ApiModelProperty(value = "待发货订单数")
    private Integer waitSendOrderCount;

    /**
     * 待收货订单数
     */
    @ApiModelProperty(value = "待收货订单数")
    private Integer waitReceiveOrderCount;

    /**
     * 总订单数
     */
    @ApiModelProperty(value = "总订单数")
    private Integer totalOrderCount;

}
