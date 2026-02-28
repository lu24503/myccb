/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.collect;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收藏商品、店铺列表
 */
@Data
@ApiModel(value = "CollectGetAllParam", description = "收藏商品、店铺列表")
public class CollectGetAllParam extends PageParam {

    /**
     * 搜索字段
     */
    @ApiModelProperty(value = "搜索字段")
    private String search;

}
