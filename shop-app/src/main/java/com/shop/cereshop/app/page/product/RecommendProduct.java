/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 种草商品返回数据实体类
 */
@Data
@ApiModel(value = "RecommendProduct", description = "种草商品返回数据实体类")
public class RecommendProduct {
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 默认规格id
     */
    @ApiModelProperty(value = "默认规格id")
    private Long skuId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;
    /**
     * 商品描述（富文本）
     */
    @ApiModelProperty(value = "商品描述（富文本）")
    private String productText;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String images;

    /**
     * 售价区间
     */
    @ApiModelProperty(value = "售价区间")
    private String section;
}
