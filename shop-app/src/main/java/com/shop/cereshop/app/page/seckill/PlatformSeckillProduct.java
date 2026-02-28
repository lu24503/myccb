/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.seckill;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 平台端秒杀商品对象
 */
@Data
@ApiModel(value = "PlatformSeckillProduct", description = "平台端秒杀商品")
public class PlatformSeckillProduct {

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * skuId
     */
    private Long skuId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片
     */
    private String productImage;

    /**
     * 原始价格
     */
    private BigDecimal originalPrice;

    /**
     * 秒杀价
     */
    private BigDecimal price;

    /**
     * 活动库存
     */
    private Integer total;

    /**
     * 销量
     */
    private Integer saleNumber;
}
