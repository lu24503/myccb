/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 店铺首页数据
 */
@Data
@ApiModel(value = "ShopIndex", description = "店铺首页数据")
public class ShopIndex {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 收藏id
     */
    @ApiModelProperty(value = "收藏id")
    private Long collectId;

    /**
     * 是否收藏 1-是 0-否
     */
    @ApiModelProperty(value = "是否收藏 1-是 0-否")
    private Integer ifCollect;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 店铺log
     */
    @ApiModelProperty(value = "店铺log")
    private String shopLogo;

    /**
     * 店铺地址
     */
    @ApiModelProperty(value = "店铺地址")
    private String shopAdress;

    /**
     * 店铺优惠券数据
     */
    @ApiModelProperty(value = "店铺优惠券数据")
    private List<ShopCoupon> coupons;

    /**
     * 秒杀活动专区数据
     */
    @ApiModelProperty(value = "秒杀活动专区数据")
    private List<ShopSeckill> shopSeckill;

    /**
     * 拼团专区数据
     */
    @ApiModelProperty(value = "拼团专区数据")
    private List<ShopGroupWork> shopGroupWork;

    /**
     * 限时折扣专区数据
     */
    @ApiModelProperty(value = "限时折扣专区数据")
    private List<ShopDiscount> shopDiscount;

    /**
     * 商品总类数
     */
    @ApiModelProperty(value = "商品总类数")
    private Integer classifyNumber;

    /**
     * 已售件数
     */
    @ApiModelProperty(value = "已售件数")
    private Integer number;

    /**
     * 粉丝数
     */
    @ApiModelProperty(value = "粉丝数")
    private Integer fansNumber;
}
