package com.shop.cereshop.app.param.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("渠道券落地页")
public class ChannelCouponDetail {

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("购买人数")
    private Integer buyUserCount;

    @ApiModelProperty("商品图片列表")
    private List<String> productImageList;

    @ApiModelProperty("商品原价")
    private BigDecimal originalPrice;

    @ApiModelProperty("满减或折扣后的价格")
    private BigDecimal price;

    @ApiModelProperty("优惠券id")
    private Long shopCouponId;

    @ApiModelProperty("优惠券类型 1-满减 2-折扣")
    private Integer couponType;

    @ApiModelProperty("满x元")
    private BigDecimal fullMoney;

    @ApiModelProperty("减y员或打z折扣")
    private BigDecimal reduceMoney;

    @ApiModelProperty("库存数量")
    private Integer stockNumber;

    @ApiModelProperty("活动开始时间")
    private String startTime;

    @ApiModelProperty("活动结束时间")
    private String endTime;

    @ApiModelProperty(value = "领取状态 0-已领取 1-已使用 2-已过期 3-未领取")
    private Integer state;

}
