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
 * 获取评价列表请求
 */
@Data
@ApiModel(value = "CommentParam", description = "获取评价列表请求")
public class CommentParam extends PageParam {

    /**
     * 是否有图 1-是 0-全部
     */
    @ApiModelProperty(value = "是否有图 1-是 0-全部")
    private String state;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Long buyerUserId;

    /**
     * 关键字搜索
     */
    @ApiModelProperty(value = "关键字搜索")
    private String search;

    /**
     * 评价类型 1-待评价 2-待追评 3-已评价
     */
    @ApiModelProperty(value = "评价类型 1-待评价 2-待追评 3-已评价")
    private Integer type;
}
