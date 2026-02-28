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
 * 店铺拼团专区数据
 */
@Data
@ApiModel(value = "ShopGroupWork", description = "店铺拼团专区数据")
public class ShopGroupWork {

    /**
     * 拼团活动id
     */
    @ApiModelProperty(value = "拼团活动id")
    private Long shopGroupWorkId;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String groupName;

    /**
     * 拼团专区商品数据
     */
    @ApiModelProperty(value = "拼团专区商品数据")
    private List<ToolProduct> groupProducts;
}
