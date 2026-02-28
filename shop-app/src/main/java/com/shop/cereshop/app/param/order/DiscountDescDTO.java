/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DiscountDescDTO {

    /** 优惠价 */
    private BigDecimal discountPrice;

    /** 优惠折扣 */
    private BigDecimal discount;

    /** 优惠数量 */
    private Integer discountNum = 0;

    /** 任选100元3件中的 100元 */
    private BigDecimal composePrice;

    /** 任选100元3件中的 3件 */
    private Integer composeNum;

    /** 总共优惠多少钱 */
    private BigDecimal discountTotal = BigDecimal.ZERO;
}
