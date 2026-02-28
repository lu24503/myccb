package com.shop.cereshop.app.param.discount;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DiscountRelateProductInfo {

    /**
     * 平台折扣id
     */
    private Long discountId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * skuId
     */
    private Long skuId;

    /**
     * 活动开始时间(商家秒杀)
     */
    private String startTime;

    /**
     * 活动结束时间(商家秒杀)
     */
    private String endTime;

    /**
     * 打多少折
     */
    private BigDecimal discount;

    /**
     * 是否预热(商家秒杀)
     */
    private Integer ifEnable;

    /**
     * 活动开始前几小时预热(商家秒杀)
     */
    private Integer enableTime;

    /**
     * 剩余库存
     */
    private Integer number;

    /**
     * 秒杀活动库存总量
     */
    private Integer total;

    /**
     * 是否限购 1-不限购 2-限购
     */
    private Integer ifLimit;

    /**
     * 限购数量
     */
    private Integer limitNumber;

    /**
     * 是否可以叠加优惠券 1-是 0-否
     */
    private Integer ifAdd;
}
