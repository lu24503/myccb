/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.order;

import com.shop.cereshop.app.page.settlement.Distribution;
import com.shop.cereshop.app.param.settlement.ProductSku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 结算查询商品参数
 */
@Data
@ApiModel(value = "ShopProductParam", description = "结算查询商品参数")
public class OrderProductParam implements Serializable {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 客户领取店铺优惠券主键id
     */
    @ApiModelProperty(value = "客户领取店铺优惠券主键id")
    private Long id;

    /**
     * 商品明细
     */
    @ApiModelProperty(value = "商品明细")
    private List<ProductSku> skus;

    /**
     * 配送方式信息
     */
    @ApiModelProperty(value = "配送方式信息")
    private Distribution distribution;

    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注")
    private String remark;
}
