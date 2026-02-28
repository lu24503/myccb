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
 * 售后详情
 */
@Data
@ApiModel(value = "AfterDetail", description = "售后详情")
public class AfterDetail {

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
     * 是否平台介入 1-是 0-否
     */
    @ApiModelProperty(value = "是否平台介入 1-是 0-否")
    private Integer isPlaformState;

    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private BigDecimal price;

    /**
     * 售后单号
     */
    @ApiModelProperty(value = "售后单号")
    private String afterFormid;

    /**
     * 商家处理留言
     */
    @ApiModelProperty(value = "商家处理留言")
    private String reason;

    /**
     * 退款原因
     */
    @ApiModelProperty(value = "退款原因")
    private String returnReason;

    /**
     * 自动关闭剩余时间
     */
    @ApiModelProperty(value = "自动关闭剩余时间")
    private long time;

    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    private String createTime;

    /**
     * 协商历史
     */
    @ApiModelProperty(value = "协商历史")
    private List<AfterHistory> afterHistory;

    /**
     * 退货地址
     */
    @ApiModelProperty(value = "退货地址")
    private String returnAdress;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String returnPerson;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String returnPhone;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "联系电话")
    private String shopName;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String shopPhone;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderFormid;

    /**
     * 支付渠道 1-微信 2-支付宝
     */
    @ApiModelProperty(value = "支付渠道 1-微信 2-支付宝")
    private String paymentMode;

    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间")
    private String paymentTime;

    /**
     * 成交时间
     */
    @ApiModelProperty(value = "联系电话")
    private String receiveTime;

    /**
     * 交易号
     */
    @ApiModelProperty(value = "交易号")
    private String transactionId;

    /**
     * 订单状态 1-待付款 2-待发货 3-待收货 4-已完成 5-已取消
     */
    @ApiModelProperty(value = "订单状态 1-待付款 2-待发货 3-待收货 4-已完成 5-已取消")
    private Integer orderState;

    /**
     * 买家售后说明
     */
    @ApiModelProperty(value = "买家售后说明")
    private String explain;

    /**
     * 买家售后图片
     */
    @ApiModelProperty("买家售后图片")
    private String image;

    /**
     * 售后图片数组
     */
    @ApiModelProperty(value = "售后图片数组")
    private List<String> images;

    /**
     * 买家退货公司id
     */
    @ApiModelProperty("买家退货公司id")
    private Long express;

    /**
     * 买家退货物流单号
     */
    @ApiModelProperty(value = "买家退货物流单号")
    private String deliverFormid;

    /**
     * 买家退货说明
     */
    @ApiModelProperty(value = "买家退货说明")
    private String deliverReason;

}
