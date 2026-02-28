/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.canvas;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 画布商品数据请求
 */
@Data
@ApiModel(value = "CanvasAppProductParam", description = "画布商品数据请求")
public class CanvasAppProductParam extends PageParam {

    /**
     * 搜索字段
     */
    @ApiModelProperty(value = "搜索字段")
    private String search;

    /**
     * 是否上架 1-上架 0-不上架
     */
    @ApiModelProperty(value = "是否上架 1-上架 0-不上架")
    private String shelveState;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long classifyId;

    @ApiModelProperty(value = "分类层级")
    private Integer classifyLevel;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 活动类型 1-拼团活动 2-秒杀活动 3-限时折扣活动
     */
    @ApiModelProperty(value = "活动类型 1-拼团活动 2-秒杀活动 3-限时折扣活动")
    private Integer type;

    /**
     * 商品id数组
     */
    @ApiModelProperty(value = "商品id数组")
    private List<Long> ids;

    /**
     * 会员等级id
     */
    @ApiModelProperty(value = "会员等级id")
    private Long memberLevelId;
}
