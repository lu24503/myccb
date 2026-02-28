/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.domain.promotion;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 营销活动列表展示对象
 */
@ApiModel("营销活动列表展示对象")
@Data
public class PromotionShowVO {

    @ApiModelProperty("营销活动的id")
    private Long id;

    @ApiModelProperty("营销活动的标签")
    private String tag;

}
