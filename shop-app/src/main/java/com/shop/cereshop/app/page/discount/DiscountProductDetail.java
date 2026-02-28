/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.discount;

import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.product.*;
import com.shop.cereshop.commons.domain.product.Sku;
import com.shop.cereshop.commons.domain.product.SkuName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 限时折扣商品返回数据
 */
@Data
@ApiModel(value = "DiscountProductDetail", description = "限时折扣商品返回数据")
public class DiscountProductDetail {

    /**
     * 限时折扣活动id
     */
    @ApiModelProperty(value = "限时折扣活动id")
    private Long shopDiscountId;

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
    private Integer ifCollect=0;

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
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long classifyId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 商品原价
     */
    @ApiModelProperty(value = "商品原价")
    private BigDecimal originalPrice;

    /**
     * 商品售价
     */
    @ApiModelProperty(value = "商品售价")
    private BigDecimal price;

    /**
     * 折扣价
     */
    @ApiModelProperty(value = "折扣价")
    private BigDecimal discountPrice;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;

    /**
     * 商品详情描述
     */
    @ApiModelProperty(value = "商品详情描述")
    private String text;

    /**
     * 商品图片数组
     */
    @ApiModelProperty(value = "商品图片数组")
    private List<String> images;

    /**
     * 商品评论信息
     */
    @ApiModelProperty(value = "商品评论信息")
    private List<BuyerComment> comments;

    /**
     * 评论关键词数据
     */
    @ApiModelProperty(value = "评论关键词数据")
    private List<CommentWord> words;

    /**
     * 商品简介
     */
    @ApiModelProperty(value = "商品简介")
    private String productBrief;

    /**
     * 规格属性名数组
     */
    @ApiModelProperty(value = "规格属性名数组")
    private List<SkuName> names;

    /**
     * 所有规格属性组合数据
     */
    @ApiModelProperty(value = "所有规格属性组合数据")
    private Map<String, Sku> map;

    /**
     * 同类商品数据（默认4条按销量排序）
     */
    @ApiModelProperty(value = "同类商品数据（默认4条按销量排序）")
    private List<Product> similarProducts;

    /**
     * 店铺优惠券数据
     */
    @ApiModelProperty(value = "店铺优惠券数据")
    private List<ProductCoupon> markTools;

    /**
     * 优惠券拼接字段
     */
    @ApiModelProperty(value = "优惠券拼接字段")
    private String couponSplicing;

    /**
     * 优惠券图片数组
     */
    @ApiModelProperty(value = "优惠券图片数组")
    private List<String> couponImages;

    /**
     * 店铺优惠券数据
     */
    @ApiModelProperty(value = "优惠券数据")
    private List<ProductCoupon> shopMarkTools;
    /**
     * 限时折扣活动截止倒计时
     */
    @ApiModelProperty(value = "限时折扣活动截止倒计时")
    private long time;

    /**
     * 限时折扣活动开始时间
     */
    @ApiModelProperty(value = "限时折扣活动开始时间")
    private String startTime;

    /**
     * 仅剩件数
     */
    @ApiModelProperty(value = "仅剩件数")
    private Integer surplusNumber;

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
}
