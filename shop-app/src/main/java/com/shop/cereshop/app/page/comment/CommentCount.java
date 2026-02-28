/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.comment;

import com.shop.cereshop.commons.domain.common.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的评论数据
 */
@Data
@ApiModel(value = "CommentCount", description = "我的评论数据")
public class CommentCount {

    /**
     * 全部评价数量
     */
    @ApiModelProperty(value = "全部评价数量")
    private Integer total;

    /**
     * 有图评价数量
     */
    @ApiModelProperty(value = "全部评价数量")
    private Integer imageTotal;

    /**
     * 列表数据
     */
    @ApiModelProperty(value = "列表数据")
    private Page<SelfComment> page;
}
