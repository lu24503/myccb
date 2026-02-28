/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取评价详情请求
 */
@Data
@ApiModel(value = "CommentIdParam", description = "获取评价详情请求")
public class CommentIdParam {

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long commentId;
}
