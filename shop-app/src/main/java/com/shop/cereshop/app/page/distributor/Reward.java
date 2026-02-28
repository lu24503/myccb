/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.distributor;

import com.shop.cereshop.commons.domain.common.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 累计奖励
 */
@Data
@ApiModel(value = "Reward", description = "累计奖励")
public class Reward {

    /**
     * 累计奖励
     */
    @ApiModelProperty(value = "累计奖励")
    private BigDecimal total;

    /**
     * 直接奖励
     */
    @ApiModelProperty(value = "直接奖励")
    private BigDecimal directPrice;

    /**
     * 间接奖励
     */
    @ApiModelProperty(value = "间接奖励")
    private BigDecimal indirectPrice;

    /**
     * 列表数据
     */
    @ApiModelProperty(value = "列表数据")
    private Page<CumulativeReward> page;
}
