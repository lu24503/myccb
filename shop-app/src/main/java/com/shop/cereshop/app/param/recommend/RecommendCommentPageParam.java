/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.recommend;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取种草评论列表请求
 */
@Data
@ApiModel(value = "RecommendCommentPageParam", description = "获取种草评论列表请求")
public class RecommendCommentPageParam extends PageParam {

    /**
     * 种草id
     */
    @ApiModelProperty(value = "种草id")
    private Long recommendId;

    /**
     * 种草评论id
     */
    @ApiModelProperty(value = "种草评论id")
    private Long recommendCommentId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 回复评论id
     */
    @ApiModelProperty(value = "回复评论id")
    private Long replyCommentId;
}
