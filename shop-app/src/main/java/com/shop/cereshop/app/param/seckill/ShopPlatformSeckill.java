/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.seckill;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopPlatformSeckill {

    private Long shopId;

    private Long seckillId;

    private Integer ifLimit;

    private Integer limitNumber;

    private BigDecimal seckillMoney;

    private Long productId;

    private Long number;

    private Long total;

    private Integer ifAdd;
}
