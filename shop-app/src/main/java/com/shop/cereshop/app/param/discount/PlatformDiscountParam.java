/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.discount;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 折扣请求对象
 */
@Data
public class PlatformDiscountParam extends PageParam {

    /**
     * 折扣id
     */
    private Long discountId;

    /**
     * 价格排序条件 1-升序 2-降序
     */
    @ApiModelProperty(value = "价格排序条件 1-升序 2-降序")
    private Integer type;

    /**
     * 销量排序条件 1-升序 2-降序
     */
    @ApiModelProperty(value = "销量排序条件 1-升序 2-降序")
    private Integer volume;
}
