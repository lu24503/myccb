/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.canvas;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 画布平台活动数据
 */
@Data
@ApiModel(value = "CanvasCoupon", description = "画布平台活动数据")
public class CanvasCoupon {

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id")
    private Long activityId;

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    private String startTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    private String endTime;

    /**
     * 优惠方式 1-满减 2-优惠券
     */
    @ApiModelProperty(value = "优惠方式 1-满减 2-优惠券")
    private Integer discountMode;

    /**
     * 满多少元
     */
    @ApiModelProperty(value = "满多少元")
    private BigDecimal fullMoney;

    /**
     * 减多少元
     */
    @ApiModelProperty(value = "减多少元")
    private BigDecimal reduceMoney;

    /**
     * 状态 0-已领取 1-已使用 2-已过期 3-未领取
     */
    @ApiModelProperty(value = "状态 0-已领取 1-已使用 2-已过期 3-未领取")
    private Integer state;

    /**
     * 是否支持积分兑换 1-支持 0-不支持
     */
    @ApiModelProperty(value = "是否支持积分兑换")
    private Integer ifCredit;

    /**
     * 兑换所需积分
     */
    @ApiModelProperty(value = "兑换所需积分")
    private Integer credit;

    /**
     * 是否同步微信卡券
     */
    @ApiModelProperty(value = "是否同步微信卡券")
    private Integer syncCard;

    /**
     * 微信卡券id
     */
    @ApiModelProperty(value = "微信卡券id")
    private String cardId;

    /**
     * 总已兑换数
     */
    @ApiModelProperty(value = "总已兑换数")
    private Integer takeCount;

    @ApiModelProperty("是否限制领取次数 1-无限次 2-限制次数")
    private Integer receiveType;

    @ApiModelProperty("限制领取次数 当receiveType = 1 时 该值为null")
    private Integer frequency;

    @ApiModelProperty("剩余库存")
    private Integer stockNumber;

    @ApiModelProperty("当前用户已领取数")
    private Integer userTakeCount;

    @ApiModelProperty("优惠券类型 1-满减 2-折扣")
    private Integer couponType;

    public Integer getCouponType() {
        return this.discountMode;
    }
}
