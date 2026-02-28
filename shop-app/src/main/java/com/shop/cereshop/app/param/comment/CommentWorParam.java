/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.comment;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 关键词查评论列表
 */
@Data
@ApiModel(value = "CommentWorParam", description = "关键词查评论列表")
public class CommentWorParam extends PageParam {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 关键词
     */
    @ApiModelProperty(value = "关键词")
    private String word;
}
