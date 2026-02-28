/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取下拉数据请求
 */
@Data
@ApiModel(value = "DictGetSelectParam", description = "获取下拉数据请求")
public class DictGetSelectParam {

    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称")
    private String dictName;
}
