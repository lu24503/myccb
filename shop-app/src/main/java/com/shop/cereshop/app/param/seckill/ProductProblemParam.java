/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.seckill;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品问答数据请求
 */
@Data
@ApiModel(value = "ProductProblemParam", description = "商品问答数据请求")
public class ProductProblemParam extends PageParam {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 提问id
     */
    @ApiModelProperty(value = "提问id")
    private Long problemId;
}
