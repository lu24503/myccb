/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.classify;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 店铺所有商品所属一级类目
 * @author
 */
@Data
@ApiModel(value = "ShopClassify", description = "店铺所有商品所属一级类目")
public class Classify {

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long classifyId;

    /**
     * 分类父节点id
     */
    @ApiModelProperty(value = "分类父节点id")
    private Long classifyPid;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String classifyName;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String classifyImage;

    /**
     * 子节点
     */
    @ApiModelProperty(value = "子节点")
    private List<Classify> childs;
}
