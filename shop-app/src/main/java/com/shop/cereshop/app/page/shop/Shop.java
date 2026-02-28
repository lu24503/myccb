/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.shop;

import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.commons.domain.common.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺商品数据
 */
@Data
@ApiModel(value = "Shop", description = "店铺商品数据")
public class Shop {

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
     * 店铺log
     */
    @ApiModelProperty(value = "店铺log")
    private String shopLogo;

    /**
     * 已售件数
     */
    @ApiModelProperty(value = "已售件数")
    private Integer number;

    /**
     * 商品列表数据
     */
    @ApiModelProperty(value = "商品列表数据")
    private Page<Product> page;
}
