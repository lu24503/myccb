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
 * 店铺限时折扣专区数据
 */
@Data
@ApiModel(value = "ShopDiscount", description = "店铺限时折扣专区数据")
public class ShopDiscount {

    /**
     * 限时折扣活动id
     */
    @ApiModelProperty(value = "限时折扣活动id")
    private Long shopDiscountId;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String discountName;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    private String startTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    private String endTime;

    /**
     * 活动开始倒计时时间（毫秒）
     */
    @ApiModelProperty(value = "活动开始倒计时时间（毫秒）")
    private long time;

    /**
     * 限时折扣商品数据
     */
    @ApiModelProperty(value = "限时折扣商品数据")
    private List<ToolProduct> discountProducts;
}
