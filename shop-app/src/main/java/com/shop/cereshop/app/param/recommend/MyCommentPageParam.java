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
 * 我的消息分页参数
 */
@Data
@ApiModel(value = "MyCommentPageParam", description = "我的消息分页参数")
public class MyCommentPageParam extends PageParam {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;
}
