/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.order;

import com.shop.cereshop.app.page.cart.CartSku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单列表数据
 */
@Data
@ApiModel(value = "Orders", description = "订单列表数据")
public class Orders {

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 店铺拼团活动id
     */
    @ApiModelProperty(value = "店铺拼团活动id")
    private Long shopGroupWorkId;

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
     * 拼单id
     */
    @ApiModelProperty(value = "拼单id")
    private Long collageId;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderFormid;

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
     * 店铺头像
     */
    @ApiModelProperty(value = "店铺头像")
    private String shopLogo;

    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String receiveName;

    /**
     * 售后状态 1-审核中 2-退款中 3-退货中 4-退款成功 5-退款失败 6-审核不通过 7-评审中 8-退货完成，拒绝退款 9-已关闭 10-审核通过
     */
    @ApiModelProperty(value = "售后状态 1-审核中 2-退款中 3-退货中 4-退款成功 5-退款失败 6-审核不通过 7-评审中 8-退货完成，拒绝退款 9-已关闭 10-审核通过")
    private String afterState;

    /**
     * 订单状态 1-待付款 2-待发货 3-待收货 4-已完成 5-已取消
     */
    @ApiModelProperty(value = "订单状态 1-待付款 2-待发货 3-待收货 4-已完成 5-已取消I")
    private Integer state;

    /**
     * 是否支付 1-是 0-否
     */
    @ApiModelProperty(value = "是否支付 1-是 0-否")
    private Integer paymentState;

    /**
     * 订单总价
     */
    @ApiModelProperty(value = "订单总价")
    private BigDecimal orderPrice;

    /**
     * 运费
     */
    @ApiModelProperty(value = "运费")
    private BigDecimal logisticsPrice;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountPrice;

    /**
     * 实付金额
     */
    @ApiModelProperty(value = "实付金额")
    private BigDecimal price;

    /**
     * 商品明细
     */
    @ApiModelProperty(value = "商品明细")
    private List<CartSku> skus;

    /**
     * 商家发货物流公司
     */
    @ApiModelProperty(value = "商家发货物流公司")
    private String express;

    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    private String deliverFormid;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String createTime;

    /**
     * 是否有未评价商品 1-是 0-否
     */
    @ApiModelProperty(value = "是否有未评价商品 1-是 0-否")
    private Integer ifNotComment;

    /**
     * 订单关闭倒计时
     */
    @ApiModelProperty(value = "订单关闭倒计时")
    private long time;

    /**
     * 定价捆绑优惠的金额
     */
    @ApiModelProperty(value = "定价捆绑优惠的金额")
    private BigDecimal pricingPrice;

    /**
     * 交易流水号
     */
    @ApiModelProperty(value = "交易流水号")
    private String transactionId;

}
