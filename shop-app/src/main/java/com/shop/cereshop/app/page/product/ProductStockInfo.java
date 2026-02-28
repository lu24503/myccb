/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductStockInfo {

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long productId;

    /**
     * 库存总数
     */
    @ApiModelProperty("库存总数")
    private Integer total;

    /**
     * 当前剩余库存数
     */
    @ApiModelProperty("当前剩余库存数")
    private Integer stockNumber;
}
