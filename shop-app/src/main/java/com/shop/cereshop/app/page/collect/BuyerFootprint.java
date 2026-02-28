/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.collect;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 我的足迹列表数据
 */
@Data
@ApiModel(value = "BuyerFootprint", description = "我的足迹列表数据")
public class BuyerFootprint {

    /**
     * 浏览时间
     */
    @ApiModelProperty(value = "浏览时间")
    private String createTime;

    /**
     * 商品明细数据
     */
    @ApiModelProperty(value = "商品明细数据")
    private List<FootprintProduct> products;
}
