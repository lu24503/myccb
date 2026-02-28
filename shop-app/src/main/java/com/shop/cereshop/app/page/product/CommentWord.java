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
 * 评论关键词数据
 */
@Data
@ApiModel(value = "CommentWord", description = "评论关键词数据")
public class CommentWord {

    /**
     * 关键词
     */
    @ApiModelProperty(value = "关键词")
    private String keyWord;

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private Integer count;
}
