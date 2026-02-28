/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺所有商品所属一级类目
 * @author
 */
@Data
@ApiModel(value = "ShopClassify", description = "店铺所有商品所属一级类目")
public class ShopClassify {

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long classifyId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String classifyName;
}
