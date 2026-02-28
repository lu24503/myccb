/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.index;

import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.utils.EmptyUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品搜索请求
 */
@Data
@ApiModel(value = "SearchParam", description = "商品搜索请求")
public class SearchParam extends PageParam {

    /**
     * 搜索内容
     */
    @ApiModelProperty(value = "搜索内容")
    private String search;

    public void setSearch(String search) {
        if (!EmptyUtils.isEmpty(search) && search.length() > 40) {
            this.search = search.substring(0, 40);
        } else {
            this.search = search;
        }
    }

    /**
     * 搜索id
     */
    @ApiModelProperty(value = "搜索id")
    private Long searchId;

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
     * 最小价格
     */
    @ApiModelProperty(value = "最小价格")
    private BigDecimal minMoney;

    /**
     * 最大价格
     */
    @ApiModelProperty(value = "最大价格")
    private BigDecimal maxMoney;

    @ApiModelProperty(value = "会员等级id")
    private Long memberLevelId;

    @ApiModelProperty(value = "分类id")
    private Long classifyId;

    @ApiModelProperty(value = "分类层级")
    private Integer classifyLevel;
}
