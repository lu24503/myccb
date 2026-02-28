/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 结算查询请求
 */
@Data
@ApiModel(value = "OrderParam", description = "结算查询请求")
public class OrderParam implements Serializable {

    /**
     * 店铺商品数据
     */
    @ApiModelProperty(value = "店铺商品数据")
    private List<OrderProductParam> shops;

    /**
     * 收货地址id
     */
    @ApiModelProperty(value = "收货地址id")
    private Long receiveId;

    /**
     * 拼单id(参与拼团才有值)（拼团订单提交参数）
     */
    @ApiModelProperty(value = "拼单id(参与拼团才有值)（拼团订单提交参数）")
    private Long collageId;

    /**
     * 店铺拼团活动id（拼团订单提交参数）
     */
    @ApiModelProperty(value = "店铺拼团活动id（拼团订单提交参数）")
    private Long shopGroupWorkId;

    /**
     * 秒杀活动id（秒杀订单提交参数）
     */
    @ApiModelProperty(value = "秒杀活动id（秒杀订单提交参数）")
    private Long shopSeckillId;

    /**
     * 限时折扣活动id（限时折扣订单提交参数）
     */
    @ApiModelProperty(value = "限时折扣活动id（限时折扣订单提交参数）")
    private Long shopDiscountId;

    /**
     * 平台秒杀活动id
     */
    @ApiModelProperty(value = "平台秒杀活动id")
    private Long platformSeckillId;

    /**
     * 平台限时折扣活动id
     */
    @ApiModelProperty(value = "平台限时折扣活动id")
    private Long platformDiscountId;

    /**
     * 请求类型 1-发起拼团 2-参与拼团 3-秒杀活动 4-限时折扣活动（拼团、秒杀、限时折扣订单提交参数）
     */
    @ApiModelProperty(value = "请求类型 1-发起拼团 2-参与拼团 3-秒杀活动 4-限时折扣活动（拼团、秒杀、限时折扣订单提交参数）")
    private Integer type;

    /**
     * 平台优惠券id
     */
    @ApiModelProperty(value = "平台优惠券id")
    private Long couponId;

    /**
     * 订单金额（计算后）
     */
    @ApiModelProperty(value = "订单金额（计算后）")
    private BigDecimal price;

    /**
     * 优惠金额(计算后)
     */
    @ApiModelProperty(value = "优惠金额(计算后)")
    private BigDecimal discountPrice=BigDecimal.ZERO;

    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注")
    private String remark;

    /**
     * 订单支付方式 1-微信 2-支付宝
     */
    @ApiModelProperty(value = "订单支付方式")
    private Integer paymentMode;

    /**
     * 支付子类型 微信支付，还对应小程序支付 app支付 H5支付等
     * 1-小程序支付 2-app支付 3-H5支付
     */
    private Integer subPaymentMode;

    /**
     * 订单来源/支付渠道：ccblife 代表建行生活
     */
    private String channel;
}
