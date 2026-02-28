/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.groupwork;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 拼团单结算查询请求
 */
@Data
@ApiModel(value = "GroupWorkSettlementParam", description = "拼团单结算查询请求")
public class GroupWorkSettlementParam extends PageParam {

    /**
     * 请求类型 1-发起拼团 2-参与拼团
     */
    @ApiModelProperty(value = "请求类型 1-发起拼团 2-参与拼团")
    private Integer type;

    /**
     * 收货地址id
     */
    @ApiModelProperty(value = "收货地址id")
    private Long receiveId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long skuId;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer number;

    /**
     * 拼单id(参与拼团才有值)
     */
    @ApiModelProperty(value = "拼单id(参与拼团才有值)")
    private Long collageId;

    /**
     * 店铺拼团活动id
     */
    @ApiModelProperty(value = "店铺拼团活动id")
    private Long shopGroupWorkId;
}
