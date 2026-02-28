/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.buyer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的提问数据
 */
@Data
@ApiModel(value = "MyProblem", description = "我的提问数据")
public class MyProblem {

    /**
     * 提问id
     */
    @ApiModelProperty(value = "提问id")
    private Long problemId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 问题内容
     */
    @ApiModelProperty(value = "问题内容")
    private String problem;

    /**
     * 几条回复
     */
    @ApiModelProperty(value = "几条回复")
    private Integer count;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;

    /**
     * 是否选中 1-是 0-否
     */
    @ApiModelProperty(value = "是否选中 1-是 0-否")
    private Integer selected;

    /**
     * show
     */
    private boolean show=false;
}
