/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新商品规格请求
 */
@Data
@ApiModel(value = "UpdateSkuParam", description = "更新商品规格请求")
public class UpdateSkuParam {

    /**
     * 原来的规格id
     */
    @ApiModelProperty(value = "原来的规格id")
    private Long skuId;

    /**
     * 修改后的规格id
     */
    @ApiModelProperty(value = "修改后的规格id")
    private Long newSkuId;

    /**
     * 修改后的购买数量
     */
    @ApiModelProperty(value = "修改后的购买数量")
    private Integer number;

}
