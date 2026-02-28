/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 活动属性
 */
@Data
@ApiModel(value = "SkuTool", description = "活动属性")
public class SkuTool {

    /**
     * 店铺拼团活动id
     */
    @ApiModelProperty(value = "店铺拼团活动id")
    private Long shopGroupWorkId;

    /**
     * 秒杀活动id
     */
    @ApiModelProperty(value = "秒杀活动id")
    private Long shopSeckillId;

    /**
     * 限时折扣活动id
     */
    @ApiModelProperty(value = "限时折扣活动id")
    private Long shopDiscountId;

    /**
     * 是否开启预热状态 1-是 0-否
     */
    @ApiModelProperty(value = "是否开启预热状态 1-是 0-否")
    private Integer ifEnable;

    /**
     * 预热时间
     */
    @ApiModelProperty(value = "预热时间")
    private Integer enableTime;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    private String startTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    private String endTime;

    /**
     * 活动状态 0-未开始 1-进行中 2-已结束
     */
    @ApiModelProperty(value = "活动状态 0-未开始 1-进行中 2-已结束")
    private Integer state;
}
