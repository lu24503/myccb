/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.seckill;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopBusinessSeckill {

    private Long shopId;

    private Long seckillId;

    private Integer ifLimit;

    private Integer limitNumber;

    private Long productId;

    private Long skuId;

    private BigDecimal seckillPrice;

    private Integer number;

    private Integer total;

    private Integer ifAdd;
}
