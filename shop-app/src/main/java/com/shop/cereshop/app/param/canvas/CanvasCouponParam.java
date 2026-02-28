/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.canvas;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 画布获取平台活动请求
 */
@Data
@ApiModel(value = "CanvasCouponParam", description = "画布获取平台活动请求")
public class CanvasCouponParam extends PageParam {

    /**
     * 搜索字段
     */
    @ApiModelProperty(value = "搜索字段")
    private String search;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 平台优惠券活动id(activityId)/店铺优惠券id(shopCouponId)
     */
    @ApiModelProperty(value = "优惠券id数组")
    private List<Long> ids;

    /**
     * 客户id
     */
    private Long buyerUserId;
}
