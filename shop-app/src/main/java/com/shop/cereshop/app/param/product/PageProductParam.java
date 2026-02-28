/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.product;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页商品请求参数
 */
@Data
@ApiModel(value = "PageProductParam", description = "分页商品请求参数")
public class PageProductParam extends PageParam {

    /**
     * 时间戳 - page = 1时生成，翻页之后直接使用page = 1的timestamp
     */
    @ApiModelProperty(value = "时间戳")
    private Long timestamp;

}
