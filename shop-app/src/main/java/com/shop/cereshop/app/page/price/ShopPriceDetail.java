/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.price;

import com.shop.cereshop.app.page.compose.ComposeProduct;
import com.shop.cereshop.commons.domain.tool.CerePriceRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 定价捆绑详情
 * @author
 */
@Data
@ApiModel(value = "ShopPriceDetail", description = "定价捆绑详情")
public class ShopPriceDetail {

    /**
     * 定价id
     */
    @ApiModelProperty(value = "定价id")
    private Long priceId;

    /**
     * 组合名称
     */
    @ApiModelProperty(value = "组合名称")
    private String composeName;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    /**
     * 组合商品数据
     */
    @ApiModelProperty(value = "组合商品数据")
    private List<ComposeProduct> composeProducts;

    /**
     * 优惠规则数据
     */
    @ApiModelProperty(value = "优惠规则数据")
    private List<CerePriceRule> rules;

    /**
     * 总商品数量
     */
    @ApiModelProperty(value = "总商品数量")
    private long total;
}
