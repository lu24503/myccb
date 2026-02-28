/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 首页类目数据
 */
@Data
@ApiModel(value = "ProductClassify", description = "首页类目数据")
public class ProductClassify {

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
