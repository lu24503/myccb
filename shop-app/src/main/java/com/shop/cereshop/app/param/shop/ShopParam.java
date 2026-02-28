/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.shop;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品搜索请求
 */
@Data
@ApiModel(value = "SearchParam", description = "商品搜索请求")
public class ShopParam extends PageParam {

    /**
     * 搜索字段
     */
    @ApiModelProperty(value = "搜索字段")
    private String search;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 是否新品 1-是 0-否
     */
    private Integer ifNew;

    /**
     * 价格排序条件 1-升序 2-降序
     */
    @ApiModelProperty(value = "价格排序条件 1-升序 2-降序")
    private Integer type;

    /**
     * 销量排序条件 1-升序 2-降序
     */
    @ApiModelProperty(value = "销量排序条件 1-升序 2-降序")
    private Integer volume;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long classifyId;

    @ApiModelProperty(value = "分类层级")
    private Integer classifyLevel;

    /**
     * 分组id
     */
    @ApiModelProperty(value = "分组id")
    private Long groupId;

    /**
     * 最小价格
     */
    @ApiModelProperty(value = "最小价格")
    private BigDecimal minMoney;

    /**
     * 最大价格
     */
    @ApiModelProperty(value = "最大价格")
    private BigDecimal maxMoney;
}
