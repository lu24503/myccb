/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.discount;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopPlatformDiscount {

    private Long shopId;

    private Long discountId;

    private Integer ifLimit;

    private Integer limitNumber;

    private BigDecimal discount;

    private Long productId;

    private Long number;

    private Long total;

    private Integer ifAdd;
}
