/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.domain.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商家活动数据
 */
@ApiModel("商家活动数据")
@Data
public class ActivityData {

    /**
     * 活动价格
     */
    private BigDecimal price;

    /**
     * 店铺拼团活动id
     */
    private Long shopGroupWorkId;

    /**
     * 秒杀活动id
     */
    private Long shopSeckillId;

    /**
     * 限时折扣活动id
     */
    private Long shopDiscountId;
}
