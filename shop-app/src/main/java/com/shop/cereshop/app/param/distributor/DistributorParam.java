/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.distributor;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺分销请求
 */
@Data
@ApiModel(value = "DistributorParam", description = "店铺分销请求")
public class DistributorParam extends PageParam {

    /**
     * 分销员id
     */
    @ApiModelProperty(value = "分销员id")
    private Long distributorId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
}
