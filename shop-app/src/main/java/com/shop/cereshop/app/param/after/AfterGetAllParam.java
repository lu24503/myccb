/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.after;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 售后列表请求
 */
@Data
@ApiModel(value = "AfterGetAllParam", description = "售后列表请求")
public class AfterGetAllParam extends PageParam {

    /**
     * 售后状态 1-售后中 2-已完成
     */
    @ApiModelProperty(value = "售后状态 1-售后中 2-已完成")
    private Integer state;
}
