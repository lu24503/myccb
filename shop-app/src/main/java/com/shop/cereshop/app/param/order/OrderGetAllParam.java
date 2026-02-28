/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.order;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取订单列表请求
 */
@Data
@ApiModel(value = "OrderGetAllParam", description = "获取订单列表请求")
public class OrderGetAllParam extends PageParam {

    /**
     * 搜索字段
     */
    @ApiModelProperty(value = "搜索字段")
    private String search;

    /**
     * 状态 null-全部 1-代付款 2-待发货 3-待收货 4-待评价 6-待成团
     */
    @ApiModelProperty(value = "状态 null-全部 1-待付款 2-待发货 3-待收货 4-待评价 6-待成团")
    private String state;

    /**
     * 客户id
     */
    private Long buyerUserId;

    /**
     * 页大小
     */
    private Integer page;
}
