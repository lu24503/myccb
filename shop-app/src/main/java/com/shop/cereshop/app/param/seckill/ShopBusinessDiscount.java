/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.seckill;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopBusinessDiscount {

    private Long shopId;

    private Long discountId;

    private Integer ifLimit;

    private Integer limitNumber;

    private Long productId;

    private Long skuId;

    private BigDecimal discount;

    private BigDecimal price;

    private Integer number;

    private Integer total;

    private Integer ifAdd;

}
