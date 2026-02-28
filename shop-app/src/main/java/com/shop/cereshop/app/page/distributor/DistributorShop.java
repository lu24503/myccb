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
 * 分销中心
 */
@Data
@ApiModel(value = "DistributorShop", description = "分销中心")
public class DistributorShop {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 分销员id
     */
    @ApiModelProperty(value = "分销员id")
    private Long distributorId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺logo")
    private String shopLogo;

    /**
     * 分销员等级名称
     */
    @ApiModelProperty(value = "分销员等级名称")
    private String levelName;

    /**
     * 关系 1-有效 0-无效
     */
    @ApiModelProperty(value = "关系 1-有效 0-无效")
    private Integer state;

    /**
     * 总收益
     */
    @ApiModelProperty(value = "总收益")
    private BigDecimal price;
}
