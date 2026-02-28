/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.canvas;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 画布商品购买数据
 */
@Data
@ApiModel(value = "CanvasProductNumber", description = "画布商品购买数据")
public class CanvasProductNumber {

    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private Long productId;

    /**
     * 销量
     */
    @ApiModelProperty(value = "销量")
    private Integer number;

}
