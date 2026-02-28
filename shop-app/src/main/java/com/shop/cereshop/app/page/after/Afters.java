/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.after;

import com.shop.cereshop.app.page.cart.CartSku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 售后列表
 */
@Data
@ApiModel(value = "Afters", description = "售后列表")
public class Afters {

    /**
     * 售后单id
     */
    @ApiModelProperty(value = "售后单id")
    private Long afterId;

    /**
     * 售后单号
     */
    @ApiModelProperty(value = "售后单号")
    private String afterFormid;

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
     * 售后状态 1-审核中 2-退款中 3-退货中 4-退款成功 5-退款失败 6-审核不通过 7-评审中 8-退货完成，拒绝退款 9-已关闭 10-审核通过
     */
    @ApiModelProperty(value = "售后状态 1-审核中 2-退款中 3-退货中 4-退款成功 5-退款失败 6-审核不通过 7-评审中 8-退货完成，拒绝退款 9-已关闭 10-审核通过")
    private Integer afterState;

    /**
     * 商品明细
     */
    @ApiModelProperty(value = "商品明细")
    private List<CartSku> skus;

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
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopLogo;

    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    private String createTime;
}
