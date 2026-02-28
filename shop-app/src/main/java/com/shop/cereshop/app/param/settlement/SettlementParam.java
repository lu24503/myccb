/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.settlement;

import com.shop.cereshop.commons.constant.IntegerEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 结算查询请求
 */
@Data
@ApiModel(value = "SettlementParam", description = "结算查询请求")
public class SettlementParam {

    /**
     * 请求类型 1-立即购买（秒杀活动、限时折扣活动） 2-购物车结算
     */
    @ApiModelProperty(value = "请求类型 1-立即购买 2-购物车结算")
    private Integer type;

    /**
     * 收货地址id
     */
    @ApiModelProperty(value = "收货地址id")
    private Long receiveId;

    /**
     * 购物车商品数据
     */
    @ApiModelProperty(value = "购物车商品数据")
    private List<ShopProductParam> shops;

    /**
     * 秒杀活动id
     */
    @ApiModelProperty(value = "秒杀活动id")
    private Long shopSeckillId;

    /**
     * 限时折扣活动id
     */
    @ApiModelProperty(value = "限时折扣活动id")
    private Long shopDiscountId;

    /**
     * 是否拼团商品单独购买 1-是 0-否
     */
    @ApiModelProperty(value = "是否拼团商品单独购买 1-是 0-否")
    private Integer ifWork;

}
