package com.shop.cereshop.app.page.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("通用优惠券返回对象")
public class CommonCoupon {

    @ApiModelProperty("来源 1-平台券 2-商家券 3-渠道券")
    private Integer source;

    @ApiModelProperty("优惠券id")
    private Long couponId;

    @ApiModelProperty("优惠券名称")
    private String couponName;

    @ApiModelProperty("领券开始时间")
    private String takeStart;

    @ApiModelProperty("领券结束时间")
    private String takeEnd;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("优惠券类型 1-满减券 2-折扣券")
    private Integer couponType;

    @ApiModelProperty("满多少元")
    private BigDecimal fullMoney;

    @ApiModelProperty("减多少元/打多少折")
    private BigDecimal reduceMoney;

    @ApiModelProperty("是否限制领取次数 1-无限次 2-限制次数")
    private Integer receiveType;

    @ApiModelProperty("限制领取次数 当receiveType = 1 时 该值为null")
    private Integer frequency;

    @ApiModelProperty("剩余库存")
    private Integer stockNumber;

    @ApiModelProperty("领取状态 0-已领取 1-已使用 2-已过期 3-未领取")
    private Integer state;

    @ApiModelProperty("用户已领取数")
    private Integer userTakeCount;
}
