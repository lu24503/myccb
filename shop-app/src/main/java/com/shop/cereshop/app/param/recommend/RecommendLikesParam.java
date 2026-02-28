/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.recommend;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 种草点赞参数
 */
@Data
@ApiModel(value = "RecommendLikesParam", description = "种草点赞参数")
@AllArgsConstructor
@NoArgsConstructor
public class RecommendLikesParam extends PageParam {

    /**
     * 种草id
     */
    @ApiModelProperty(value = "种草id")
    private Long recommendId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 类型： 1、点赞 2、取消
     */
    @ApiModelProperty(value = "类型： 1、点赞 2、取消")
    private Integer type;

}
