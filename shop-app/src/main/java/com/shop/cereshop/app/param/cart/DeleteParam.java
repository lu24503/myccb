/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 删除购物车商品请求
 */
@Data
@ApiModel(value = "DeleteParam", description = "删除购物车商品请求")
public class DeleteParam {

    /**
     * 商品规格id数组
     */
    @ApiModelProperty(value = "商品规格id数组")
    private List<Long> ids;
}
