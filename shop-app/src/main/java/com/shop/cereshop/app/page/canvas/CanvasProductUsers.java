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
 * 画布商品付款人数
 */
@Data
@ApiModel(value = "CanvasProductUsers", description = "画布商品付款人数")
public class CanvasProductUsers {

    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private Long productId;

    /**
     * 付款人数
     */
    @ApiModelProperty(value = "付款人数")
    private Integer users;


}
