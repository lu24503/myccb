/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.collect;

import com.shop.cereshop.app.page.index.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 收藏店铺数据
 */
@Data
@ApiModel(value = "CollectShop", description = "收藏店铺数据")
public class CollectShop {

    /**
     * 收藏id
     */
    @ApiModelProperty(value = "收藏id")
    private Long collectId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺logo")
    private String shopLogo;

    /**
     * 关注人数
     */
    @ApiModelProperty(value = "关注人数")
    private Integer person;

    /**
     * 是否选中 1-是 0-否
     */
    @ApiModelProperty(value = "是否选中 1-是 0-否")
    private Integer selected;

    /**
     * 商品列表
     */
    @ApiModelProperty(value = "商品列表")
    private List<Product> productList;
}
