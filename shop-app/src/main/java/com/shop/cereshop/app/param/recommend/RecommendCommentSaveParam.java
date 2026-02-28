/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * RecommendCommentSaveParam
 */
@Data
@ApiModel(value = "RecommendCommentSaveParam", description = "种草评论保存请求")
public class RecommendCommentSaveParam {

    /**
     * 种草id
     */
    @ApiModelProperty(value = "种草id")
    private Long recommendId;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    private String content;

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long recommendCommentId;

    /**
     * 评论用户id
     */
    @ApiModelProperty(value = "评论用户id")
    private Long userId;

}
