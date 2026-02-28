/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.seckill;

import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.product.Classify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 秒杀专区首页数据
 */
@Data
@ApiModel(value = "SeckillkIndex", description = "秒杀专区首页数据")
public class SeckillkIndex {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 店铺log
     */
    @ApiModelProperty(value = "店铺log")
    private String shopLogo;

    /**
     * 购物车数量
     */
    @ApiModelProperty(value = "购物车数量")
    private Integer number;

    /**
     * 秒杀活动截止倒计时
     */
    @ApiModelProperty(value = "秒杀活动截止倒计时")
    private long time;
    /**
     * 秒杀活动预热倒计时
     */
    @ApiModelProperty(value = "秒杀活动预热倒计时")
    private long enableTime;


    /**
     * 活动状态 0-未开始 1-进行中 2-已结束
     */
    private Integer state;

    /**
     * 分类层级数据
     */
    @ApiModelProperty(value = "分类层级数据")
    private List<Classify> classifies;

    /**
     * 秒杀专区商品数据
     */
    @ApiModelProperty(value = "秒杀专区商品数据")
    private Page<ToolProduct> page;

    /**
     * 活动商品限制数量
     */
    @ApiModelProperty(value = "活动商品限制数量")
    private Integer limitNumber;
}
