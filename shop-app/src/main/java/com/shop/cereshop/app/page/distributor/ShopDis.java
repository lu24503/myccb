/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.distributor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 店铺分销数据
 */
@Data
@ApiModel(value = "ShopDis", description = "店铺分销数据")
public class ShopDis {

    /**
     * 客户头像
     */
    @ApiModelProperty(value = "客户头像")
    private String headImage;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String name;

    /**
     * 累计奖励
     */
    @ApiModelProperty(value = "累计奖励")
    private BigDecimal cumulative;

    /**
     * 未结算奖励
     */
    @ApiModelProperty(value = "未结算奖励")
    private BigDecimal unsettled;

    /**
     * 累计客户
     */
    @ApiModelProperty(value = "累计客户")
    private Integer users;

    /**
     * 累计分销员
     */
    @ApiModelProperty(value = "累计分销员")
    private Integer distributors;
}
