/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.after;

import com.shop.cereshop.app.param.settlement.ProductSku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 申请售后请求
 */
@Data
@ApiModel(value = "AfterParam", description = "申请售后请求")
public class AfterParam {

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 售后类型  1-仅退款  2-退货退款
     */
    @ApiModelProperty(value = "售后类型  1-仅退款  2-退货退款")
    private Integer afterType;

    /**
     * 是否全选  0-否  1-全选
     */
    @ApiModelProperty(value = "是否全选  0-否  1-全选")
    private Integer isAllSelect;

    /**
     * 商品明细
     */
    @ApiModelProperty(value = "商品明细")
    private List<ProductSku> skus;

    /**
     * （货物状态）是否收到货 1-是 0-否
     */
    @ApiModelProperty(value = "（货物状态）是否收到货 1-是 0-否")
    private Integer goodsState;

    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private BigDecimal price;

    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String explain;

    /**
     * 退款原因
     */
    @ApiModelProperty(value = "退款原因")
    private String returnReason;

    /**
     * 图片地址（多个以逗号隔开）
     */
    @ApiModelProperty(value = "图片地址（多个以逗号隔开）")
    private String image;
}
