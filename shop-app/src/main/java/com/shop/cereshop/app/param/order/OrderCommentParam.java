/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 评论请求
 */
@Data
@ApiModel(value = "OrderCommentParam", description = "评论请求")
public class OrderCommentParam{

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long commentId;

    /**
     * 图片地址（多个以逗号隔开）
     */
    @ApiModelProperty(value = "图片地址（多个以逗号隔开）")
    private String image;

    /**
     * 评论
     */
    @ApiModelProperty(value = "评论")
    private String comment;

    /**
     * 商品满意度 1-一星 2-二星 3-三星 4-四星 5-五星
     */
    @ApiModelProperty(value = "商品满意度 1-一星 2-二星 3-三星 4-四星 5-五星")
    private Integer star;

    /**
     * 描述相符 1-一星 2-二星 3-三星 4-四星 5-五星
     */
    @ApiModelProperty(value = "商品满意度 1-一星 2-二星 3-三星 4-四星 5-五星")
    private Integer des;

    /**
     * 物流服务 1-一星 2-二星 3-三星 4-四星 5-五星
     */
    @ApiModelProperty(value = "商品满意度 1-一星 2-二星 3-三星 4-四星 5-五星")
    private Integer delivery;

    /**
     * 服务态度 1-一星 2-二星 3-三星 4-四星 5-五星
     */
    @ApiModelProperty(value = "商品满意度 1-一星 2-二星 3-三星 4-四星 5-五星")
    private Integer attitude;

    /**
     * 商品印象
     */
    @ApiModelProperty(value = "商品印象")
    private String impression;
}
