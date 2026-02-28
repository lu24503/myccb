package com.shop.cereshop.app.page.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("可积分兑换的优惠券")
public class CreditCoupon {

    @ApiModelProperty("优惠券id")
    private Long couponId;

    @ApiModelProperty("优惠类型 1-满减券 2-折扣券")
    private Integer couponType;

    @ApiModelProperty("使用门槛满多少元，无门槛为0")
    private BigDecimal fullMoney;

    @ApiModelProperty("优惠内容减多少元/打多少折")
    private BigDecimal reduceMoney;

    @ApiModelProperty("状态 0-未兑换 1-已兑换")
    private Integer state;

    @ApiModelProperty("兑换所需积分")
    private Integer credit;

    @ApiModelProperty("库存量")
    private Integer stockNumber;

    @ApiModelProperty("已兑换多少人")
    private Integer takeCount;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

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
}
