/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.collect;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 删除足迹请求
 */
@Data
@ApiModel(value = "FootprintIdParam", description = "删除足迹请求")
public class FootprintIdParam {

    /**
     * 商品id数组
     */
    @ApiModelProperty(value = "商品id数组")
    private List<Long> ids;

    /**
     * 浏览时间数组
     */
    @ApiModelProperty(value = "浏览时间数组")
    private List<String> times;
}
