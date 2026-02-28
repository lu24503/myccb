/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.cart;

import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.commons.domain.product.Sku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 购物车商品明细数据
 */
@Data
@ApiModel(value = "CartSku", description = "购物车商品明细数据")
public class CartSku {

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long commentId;

    /**
     * 是否追评 1-是 0-否
     */
    @ApiModelProperty(value = "是否追评 1-是 0-否")
    private Integer ifAdd;

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
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 原价
     */
    @ApiModelProperty(value = "原价")
    private BigDecimal originalPrice;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价")
    private BigDecimal price;

    /**
     * 实际付款总价
     */
    @ApiModelProperty(value = "实际付款总价")
    private BigDecimal actualPrice;

    /**
     * 商品重量
     */
    @ApiModelProperty(value = "商品重量")
    private BigDecimal weight;

    @ApiModelProperty("是否允许积分兑换")
    private Integer ifCredit;

    @ApiModelProperty("一笔订单中，该商品最多抵扣多少金额")
    private Integer creditLimit;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer number;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private Integer stockNumber;

    /**
     * 商品小计
     */
    @ApiModelProperty(value = "商品小计")
    private BigDecimal total;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;

    /**
     * SKU
     */
    @ApiModelProperty(value = "SKU")
    private String SKU;

    /**
     * 规格值拼接字符串
     */
    private String value;

    /**
     * 规格值数据
     */
    @ApiModelProperty(value = "规格值数据")
    private List<String> values;

    /**
     * 是否需要物流
     */
    @ApiModelProperty(value = "是否需要物流")
    private Integer ifLogistics;

    /**
     * 是否允许超卖 1-是 0-否
     */
    @ApiModelProperty(value = "是否允许超卖 1-是 0-否")
    private Integer ifOversold;

    /**
     * 选中状态 1-选中 0-未选中
     */
    @ApiModelProperty(value = "选中状态 1-选中 0-未选中")
    private Integer selected;

    /**
     * 所属活动 0-常规商品 1-拼团活动 2-秒杀活动 3-限时折扣活动 4-平台秒杀 5-平台折扣 6-定价捆绑 7-组合捆绑 8-场景营销 9-会员价
     */
    @ApiModelProperty(value = "所属活动 0-常规商品 1-拼团活动 2-秒杀活动 3-限时折扣活动 4-平台秒杀 5-平台折扣 6-定价捆绑 7-组合捆绑 8-场景营销 9-会员价")
    private Integer activityType;

    /**
     * 是否允许申请售后 1-允许 0-不允许
     */
    private Integer ifAfter;

    /**
     * 售后状态 1-审核中 2-退款中 3-退货中 4-退款成功 5-退款失败 6-审核不通过 7-评审中 8-退货完成，拒绝退款 9-已关闭 10-审核通过
     */
    @ApiModelProperty(value = "售后状态 1-审核中 2-退款中 3-退货中 4-退款成功 5-退款失败 6-审核不通过 7-评审中 8-退货完成，拒绝退款 9-已关闭 10-审核通过")
    private Integer afterState;

    /**
     * 是否上架 1-是 0-否
     */
    @ApiModelProperty(value = "是否上架 1-是 0-否")
    private Integer shelveState;

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
     * 是否使用会员价
     */
    @ApiModelProperty(value = "是否使用会员价")
    private boolean useMember;

    /**
     * 定价捆绑id
     */
    @ApiModelProperty(value = "定价捆绑id")
    private Long priceId;

    /**
     * 组合捆绑id
     */
    @ApiModelProperty(value = "组合捆绑id")
    private Long composeId;

    /**
     * 组合捆绑id列表
     */
    private List<Long> composeIdList;

    /**
     * 场景营销id
     */
    @ApiModelProperty(value = "场景营销id")
    private Long sceneId;

    /**
     * 商品所有组合规格数据
     */
    @ApiModelProperty("商品所有组合规格数据")
    private Map<String, Sku> map;

    /**
     * 是否允许叠加优惠券
     */
    @ApiModelProperty("是否允许叠加优惠券")
    private Integer ifCouponAdd;

    /**
     * 平台优惠券数据
     */
    @ApiModelProperty(value = "平台优惠券数据")
    private List<ProductCoupon> markTools;

    /**
     * 店铺优惠券数据
     */
    @ApiModelProperty(value = "店铺优惠券数据")
    private List<ProductCoupon> shopMarkTools;

    /**
     * 当activityType有值时，相应的活动id可以设置到这里，方便取值
     */
    private Long activityId;

    /**
     * 当前sku抵扣积分
     */
    @ApiModelProperty(value = "当前sku抵扣积分")
    private Integer useCredit;

    /**
     * 当前sku积分抵扣的金额
     */
    @ApiModelProperty(value = "当前sku积分抵扣的金额")
    private BigDecimal useCreditAmount;

    /**
     * 运费价格
     */
    @ApiModelProperty(value = "运费价格")
    private BigDecimal logisticsPrice;

    /**
     * 定价捆绑的优惠金额
     */
    @ApiModelProperty(value = "定价捆绑的优惠金额")
    private BigDecimal pricingPrice;

    /**
     * 平台优惠券优惠的金额
     */
    @ApiModelProperty(value = "平台优惠券优惠的金额")
    private BigDecimal discountPrice;

    /**
     * 商家优惠券优惠的金额
     */
    @ApiModelProperty(value = "商家优惠券优惠的金额")
    private BigDecimal shopDiscountPrice;

    /**
     * 平台优惠券id 有值则代表该sku享受了平台优惠券
     */
    @ApiModelProperty(value = "平台优惠券id")
    private Long buyerCouponId;

    /**
     * 商家优惠券id 有值就代表该sku享受了商家优惠券
     */
    @ApiModelProperty(value = "商家优惠券id")
    private Long buyerShopCouponId;

}

