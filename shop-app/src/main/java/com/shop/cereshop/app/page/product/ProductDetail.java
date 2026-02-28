/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.product;

import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.commons.domain.buyer.CereBuyerReceive;
import com.shop.cereshop.commons.domain.collage.CollageOrder;
import com.shop.cereshop.commons.domain.product.Sku;
import com.shop.cereshop.commons.domain.product.SkuName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品返回数据
 */
@Data
@ApiModel(value = "ProductDetail", description = "商品返回数据")
public class ProductDetail {

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
     * 秒杀活动id
     */
    @ApiModelProperty(value = "秒杀活动id")
    private Long platformSeckillId;

    /**
     * 平台限时折扣活动id
     */
    @ApiModelProperty(value = "平台限时折扣活动id")
    private Long platformDiscountId;

    /**
     * 活动仅剩件数
     */
    @ApiModelProperty(value = "活动仅剩件数")
    private Integer surplusNumber;

    /**
     * 活动限制数量总数
     */
    @ApiModelProperty(value = "活动限制数量总数")
    private Integer total;

    /**
     * 秒杀活动开始时间
     */
    @ApiModelProperty(value = "秒杀活动开始时间")
    private String effectiveStart;

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
     * 商品总类数
     */
    @ApiModelProperty(value = "商品总类数")
    private Integer classifyNumber;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId")
    private Long skuId;

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
     * 售价
     */
    @ApiModelProperty(value = "售价")
    private BigDecimal price;

    /**
     * 当商品参加拼团活动时 price变成 拼团价 salePrice放原来的售价
     */
    @ApiModelProperty(value = "单独购买价")
    private BigDecimal salePrice;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;

    /**
     * 收货地址
     */
    @ApiModelProperty(value = "收货地址")
    private CereBuyerReceive receive;

    /**
     * 快递费用
     */
    @ApiModelProperty(value = "快递费用")
    private BigDecimal logisticsPrice;

    /**
     * 付款人数
     */
    @ApiModelProperty(value = "付款人数")
    private Integer users;

    /**
     * 店铺总已售件数
     */
    @ApiModelProperty(value = "店铺总已售件数")
    private Integer number;

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
     * 平台优惠券数据
     */
    @ApiModelProperty(value = "优惠券数据")
    private List<ProductCoupon> markTools;

    /**
     * 店铺优惠券数据
     */
    @ApiModelProperty(value = "优惠券数据")
    private List<ProductCoupon> shopMarkTools;

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
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;

    /**
     * 商品简介
     */
    @ApiModelProperty(value = "商品简介")
    private String productBrief;

    /**
     * 同类商品数据（默认4条按销量排序）
     */
    @ApiModelProperty(value = "同类商品数据（默认4条按销量排序）")
    private List<Product> similarProducts;

    /**
     * 所属活动 0-常规商品 1-拼团活动 2-秒杀活动 3-限时折扣活动 4-平台秒杀 5-平台折扣 6-定价捆绑 7-组合捆绑 8-场景营销 9-会员价
     */
    @ApiModelProperty(value = "所属活动 0-常规商品 1-拼团活动 2-秒杀活动 3-限时折扣活动 4-平台秒杀 5-平台折扣 6-定价捆绑 7-组合捆绑 8-场景营销 9-会员价")
    private Integer activityType;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    /**
     * 成团人数
     */
    @ApiModelProperty(value = "成团人数")
    private Integer person;

    /**
     * 拼单数据
     */
    @ApiModelProperty(value = "拼单数据")
    private List<CollageOrder> collageOrders;

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
     * 活动结束倒计时
     */
    @ApiModelProperty(value = "活动结束倒计时")
    private long time=10000;

    /**
     * 是否正在进行活动预热 1-是 0-否
     */
    @ApiModelProperty(value = "是否正在进行活动预热 1-是 0-否 ")
    private Integer ifEnable;

    /**
     * 规格名数组
     */
    @ApiModelProperty(value = "规格名数组")
    private List<SkuName> names;

    /**
     * 商品所有组合规格数据
     */
    @ApiModelProperty(value = "商品所有组合规格数据")
    private Map<String, Sku> map;

    /**
     * 是否上架 1-是 0-否
     */
    @ApiModelProperty(value = "是否上架 1-是 0-否")
    private Integer shelveState;

    /**
     * 是否支持花呗分期
     */
    @ApiModelProperty(value = "是否支持花呗分期 1-是 0-否")
    private Integer ifHuabei;

    /**
     * 场景营销id
     */
    @ApiModelProperty(value = "场景营销id")
    private Long sceneId;

    /**
     * 场景营销名称
     */
    @ApiModelProperty(value = "场景营销名称")
    private String sceneName;

    /**
     * 场景营销包邮
     */
    @ApiModelProperty(value = "场景营销包邮")
    private Integer sceneFreeShipping;

    /**
     * 场景营销折扣
     */
    @ApiModelProperty(value = "场景营销折扣")
    private BigDecimal sceneDiscount;

    /**
     * 活动是否允许优惠券叠加
     */
    @ApiModelProperty(value = "活动是否允许优惠券叠加")
    private Integer ifAdd;

    /**
     * 定价捆绑活动id
     */
    @ApiModelProperty(value = "定价捆绑活动id")
    private Long priceId;

    /**
     * 是否需要物流 1-是 0-否
     */
    @ApiModelProperty(value = "是否需要物流 1-是 0-否")
    private Integer ifLogistics;

    /**
     * 重量
     */
    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量")
    private Integer limitNumber;
}
