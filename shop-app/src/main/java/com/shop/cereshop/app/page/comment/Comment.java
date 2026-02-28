/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 评论数据
 */
@Data
@ApiModel(value = "Comment", description = "评论数据")
public class Comment {

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long commentId;

    /**
     * 图片地址（多个以逗号隔开）
     */
    private String image;

    /**
     * 图片数组
     */
    @ApiModelProperty(value = "图片数组")
    private List<String> images;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    private String comment;

    /**
     * 商品印象
     */
    @ApiModelProperty(value = "商品印象")
    private String impression;

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
}
