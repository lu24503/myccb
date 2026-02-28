/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("支付有礼返回对象")
public class OrderPoliteDTO {

    /**
     * 成长值
     */
    @ApiModelProperty("成长值")
    private Integer growth;

    /**
     * 积分
     */
    @ApiModelProperty("积分")
    private Integer credit;

    /**
     * 支付有礼得到的券
     */
    @ApiModelProperty("优惠券列表")
    private List<OrderPoliteCouponDTO> couponList;

}
