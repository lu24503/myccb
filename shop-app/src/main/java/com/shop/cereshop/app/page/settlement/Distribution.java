/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.settlement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 结算查询返回配送数据
 */
@Data
@ApiModel(value = "Distribution", description = "结算查询返回配送数据")
public class Distribution implements Serializable {

    /**
     * 物流方案id
     */
    @ApiModelProperty(value = "物流方案id")
    private Long logisticsId;

    /**
     * 配送方式
     */
    @ApiModelProperty(value = "配送方式")
    private String distributionName;

    /**
     * 运费
     */
    @ApiModelProperty(value = "运费")
    private BigDecimal distributionPrice;
}
