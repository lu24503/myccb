/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品请求参数
 */
@Data
@ApiModel(value = "ProductParam", description = "商品请求参数")
public class ProductParam {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 访问终端 1-APP 2-微信小程序 3-H5 4-支付宝小程序
     */
    @ApiModelProperty(value = "访问终端 1-APP 2-微信小程序 3-H5 4-支付宝小程序")
    private Integer terminal;

    /**
     * 操作系统 1-Android 2-IOS
     */
    @ApiModelProperty(value = "操作系统 1-Android 2-IOS")
    private Integer system;

    /**
     * 所在地区
     */
    @ApiModelProperty(value = "所在地区")
    private String city;

    /**
     * 拼团活动id
     */
    @ApiModelProperty(value = "拼团活动id")
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
     * 秒杀活动id
     */
    @ApiModelProperty(value = "秒杀活动id")
    private Long platformSeckillId;

    /**
     * 平台限时折扣活动id
     */
    @ApiModelProperty(value = "平台限时折扣活动id")
    private Long platformDiscountId;
}
