/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.classify;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询分类商品请求
 */
@Data
@ApiModel(value = "ClassifyProductParam", description = "查询分类商品请求")
public class ClassifyProductParam extends PageParam {

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
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 用户id
     */
    private Long buyerUserId;

    /**
     * 会员等级id
     */
    private Long memberLevelId;
}
