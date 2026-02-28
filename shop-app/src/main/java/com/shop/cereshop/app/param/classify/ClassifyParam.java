/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.classify;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询分类请求
 */
@Data
@ApiModel(value = "ClassifyParam", description = "查询分类请求")
public class ClassifyParam {

    /**
     * 分类父节点id(查一级类目时不传值)
     */
    @ApiModelProperty(value = "分类父节点id(查一级类目时不传值)")
    private Long classifyId;
}
