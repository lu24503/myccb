/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.renovation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 店铺装修请求
 */
@Data
@ApiModel(value = "RenovationParam", description = "店铺装修请求")
public class RenovationParam {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 活动id数组
     */
    @ApiModelProperty(value = "活动id数组")
    private List<Long> ids;
}
