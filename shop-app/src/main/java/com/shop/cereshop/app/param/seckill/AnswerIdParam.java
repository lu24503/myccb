/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 回答id参数
 */
@Data
@ApiModel(value = "AnswerIdParam", description = "回答id参数")
public class AnswerIdParam {

    /**
     * 回答id数组
     */
    @ApiModelProperty(value = "回答id数组")
    private List<Long> ids;
}
