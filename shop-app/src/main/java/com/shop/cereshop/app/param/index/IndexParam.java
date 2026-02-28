/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 首页请求
 */
@Data
@ApiModel(value = "IndexParam", description = "首页请求")
public class IndexParam {

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id")
    private Long activityId;

}
