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
 * 取消收藏
 */
@Data
@ApiModel(value = "CollectIdParam", description = "取消收藏")
public class CollectIdParam {

    /**
     * 收藏id数组
     */
    @ApiModelProperty(value = "收藏id数组")
    private List<Long> ids;
}
