/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品提问数据
 */
@Data
@ApiModel(value = "SeckillProductProblem", description = "商品提问数据")
public class SeckillProductProblem {

    /**
     * 提问id
     */
    @ApiModelProperty(value = "提问id")
    private Long problemId;

    /**
     * 提问用户id
     */
    @ApiModelProperty(value = "提问用户id")
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
     * 问题内容
     */
    @ApiModelProperty(value = "问题内容")
    private String problem;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String name;

    /**
     * 提问用户头像
     */
    @ApiModelProperty(value = "提问用户头像")
    private String headImage;

    /**
     * 提问时间
     */
    @ApiModelProperty(value = "提问时间")
    private String createTime;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;

    /**
     * 是否展示回答按钮 1-是 0-否
     */
    @ApiModelProperty(value = "是否展示回答按钮 1-是 0-否")
    private Integer ifAnswer;

    /**
     * 是否选中 1-是 0-否
     */
    @ApiModelProperty(value = "是否选中 1-是 0-否")
    private Integer selected;

    /**
     * 回答数据明细
     */
    @ApiModelProperty(value = "回答数据明细")
    private List<SeckillProductAnswer> answers;
}
