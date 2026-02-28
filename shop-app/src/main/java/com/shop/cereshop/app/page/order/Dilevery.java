/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物流明细返回数据实体类
 */
@Data
@ApiModel(value = "Dilevery", description = "物流明细返回数据实体类")
public class Dilevery {

    /**
     * 时间
     */
    @ApiModelProperty(value = "时间")
    private String time;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String reason;
}
