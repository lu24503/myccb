/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.price;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopPriceWithRule {

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 定价捆绑活动id
     */
    private Long priceId;

    /**
     * 任选number件
     */
    private Integer number;

    /**
     * 定价price元
     */
    private BigDecimal price;

}
