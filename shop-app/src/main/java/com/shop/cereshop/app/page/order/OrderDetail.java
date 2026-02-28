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
 * 订单详情数据
 */
@Data
@ApiModel(value = "OrderDetail", description = "订单详情数据")
public class OrderDetail {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 售后状态 1-审核中 2-退款中 3-退货中 4-退款成功 5-退款失败 6-审核不通过 7-评审中 8-退货完成，拒绝退款 9-已关闭 10-审核通过
     */
    @ApiModelProperty(value = "售后状态 1-审核中 2-退款中 3-退货中 4-退款成功 5-退款失败 6-审核不通过 7-评审中 8-退货完成，拒绝退款 9-已关闭 10-审核通过")
    private String afterState;

    /**
     * 订单状态 1-待付款 2-待发货 3-待收货 4-已完成 5-已关闭
     */
    @ApiModelProperty(value = "订单状态 1-待付款 2-待发货 3-待收货 4-已完成 5-已关闭")
    private Integer state;

    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String receiveName;

    /**
     * 收货人手机号
     */
    @ApiModelProperty(value = "收货人手机号")
    private String receivePhone;

    /**
     * 收货地址
     */
    @ApiModelProperty(value = "收货地址")
    private String receiveAdress;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "收货地址")
    private String address;

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
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderFormid;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 自动关闭剩余时间
     */
    @ApiModelProperty(value = "自动关闭剩余时间")
    private long time;

    /**
     * 订单总价
     */
    @ApiModelProperty(value = "订单总价")
    private BigDecimal orderPrice;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal price;

    /**
     * 付款时间
     */
    @ApiModelProperty(value = "付款时间")
    private String paymentTime;

    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间")
    private String dileveryTime;

    /**
     * 成交时间
     */
    @ApiModelProperty(value = "成交时间")
    private String receiveTime;

    /**
     * 商品明细
     */
    @ApiModelProperty(value = "商品明细")
    private List<CartSku> skus;

    /**
     * 快递公司
     */
    @ApiModelProperty(value = "快递公司")
    private String express;

    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    private String deliverFormid;

    /**
     * 店铺负责人电话
     */
    @ApiModelProperty(value = "店铺负责人电话")
    private String chargePersonPhone;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private String paymentMode;

    /**
     * 交易流水号
     */
    @ApiModelProperty(value = "交易流水号")
    private String transactionId;

    /**
     * 拼团活动id
     */
    @ApiModelProperty(value = "拼团活动id")
    private Long shopGroupWorkId;

    /**
     * 拼单id
     */
    @ApiModelProperty(value = "拼单id")
    private Long collageId;

    /**
     * 是否支付 1-是 0-否
     */
    @ApiModelProperty(value = "是否支付 1-是 0-否")
    private Integer paymentState;

    /**
     * 拼团信息
     */
    private CollageDetail collageDetail;

    @ApiModelProperty(value = "售后创建时间")
    private String afterCreateTime;
}
