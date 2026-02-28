/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 优惠券数据
 */
@Data
@ApiModel(value = "ProductCoupon", description = "优惠券数据")
public class ProductCoupon {

    /**
     * 客户领取店铺优惠券主键id
     */
    @ApiModelProperty(value = "客户领取店铺优惠券主键id")
    private Long id;

    /**
     * 店铺优惠券id
     */
    @ApiModelProperty(value = "店铺优惠券id")
    private Long shopCouponId;

    /**
     * 平台优惠券id
     */
    @ApiModelProperty(value = "平台优惠券id")
    private Long couponId;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Long buyerUserId;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id")
    private Long activityId;

    /**
     * 活动图片
     */
    @ApiModelProperty(value = "活动图片")
    private String image;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;

    /**
     * 优惠方式 1-满减 2-优惠券
     */
    @ApiModelProperty(value = "优惠方式 1-满减 2-优惠券")
    private Integer discountMode;

    /**
     * 店铺优惠券类型 1-满减券 2-折扣券
     */
    @ApiModelProperty(value = "店铺优惠券类型 1-满减券 2-折扣券")
    private Integer couponType;

    /**
     * 适用商品 1-全部商品 2-指定商品可用 3-指定商品不可用
     */
    @ApiModelProperty(value = "适用商品 1-全部商品 2-指定商品可用 3-指定商品不可用")
    private Integer applyType;

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
     * 优惠描述(无门槛使用或者满多少元使用)
     */
    @ApiModelProperty(value = "优惠描述(无门槛使用或者满多少元使用)")
    private String content;

    /**
     * 状态 0-已领取 1-已使用 2-已过期 3-未领取
     */
    @ApiModelProperty(value = "状态 0-已领取 1-已使用 2-已过期 3-未领取")
    private Integer state;

    /**
     * 去重字段 活动id-满多少元
     */
    @ApiModelProperty(value = "去重字段 活动id-满多少元")
    private String distinct;

    /**
     * 限制领取次数
     */
    @ApiModelProperty(value = "限制领取次数")
    private Integer frequency;

    /**
     * 是否允许叠加平台优惠 1-是 0-否
     */
    @ApiModelProperty(value = "是否允许叠加平台优惠 1-是 0-否")
    private Integer ifAdd;

    /**
     * 优惠券关联商品id数组
     */
    @ApiModelProperty(value = "优惠券关联商品id数组")
    private List<Long> ids;

    /**
     * 满多少元
     */
    @ApiModelProperty(value = "满多少元")
    private BigDecimal threshold;

    /**
     * 减多少元
     */
    @ApiModelProperty(value = "减多少元")
    private BigDecimal couponContent;

    /**
     * 优惠券已领取总数
     */
    @ApiModelProperty(value = "优惠券已领取总数")
    private Integer count;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private Integer stockNumber;

    @ApiModelProperty(value = "是否已同步创建微信卡券 1-同步 0-不同步")
    private String syncCard;

    @ApiModelProperty(value = "关联的微信卡券id")
    private String cardId;

    @ApiModelProperty(value = "领取时间")
    private String createTime;
}
