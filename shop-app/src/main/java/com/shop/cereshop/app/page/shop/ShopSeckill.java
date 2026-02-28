/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.shop;

import com.shop.cereshop.app.page.tool.ToolProduct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 店铺秒杀专区数据
 */
@Data
@ApiModel(value = "ShopSeckill", description = "店铺秒杀专区数据")
public class ShopSeckill {

    /**
     * 秒杀活动id
     */
    @ApiModelProperty(value = "秒杀活动id")
    private Long shopSeckillId;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String seckillName;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    private String effectiveStart;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    private String effectiveEnd;

    /**
     * 活动开始倒计时时间（毫秒）
     */
    @ApiModelProperty(value = "活动开始倒计时时间（毫秒）")
    private long time;

    /**
     * 秒杀专区商品数据
     */
    @ApiModelProperty(value = "秒杀专区商品数据")
    private List<ToolProduct> seckillProducts;
}
