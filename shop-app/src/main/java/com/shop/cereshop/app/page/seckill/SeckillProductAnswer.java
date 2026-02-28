/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品回答数据
 */
@Data
@ApiModel(value = "SeckillProductAnswer", description = "商品回答数据")
public class SeckillProductAnswer {

    /**
     * 回答id
     */
    @ApiModelProperty(value = "回答id")
    private Long answerId;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Long buyerUserId;

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
     * 回答内容
     */
    @ApiModelProperty(value = "回答内容")
    private String answer;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String name;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String headImage;

    /**
     * 是否选中 1-是 0-否
     */
    @ApiModelProperty(value = "是否选中 1-是 0-否")
    private Integer selected;

    /**
     * 回答时间
     */
    @ApiModelProperty(value = "回答时间")
    private String createTime;
}
