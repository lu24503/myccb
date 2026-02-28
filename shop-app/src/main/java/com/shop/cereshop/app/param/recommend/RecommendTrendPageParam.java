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

import java.util.List;

/**
 * 获取种草动态列表请求
 */
@Data
@ApiModel(value = "RecommendTrendPageParam", description = "获取种草动态列表请求")
public class RecommendTrendPageParam extends PageParam {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private Integer fileType;

    /**
     * 种草类型
     */
    @ApiModelProperty(value = "种草类型")
    private Integer recommendType;

    /**
     * 收藏店铺id合集
     */
    @ApiModelProperty(value = "收藏店铺id合集")
    private List<Long> collectShopIds;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;
}
