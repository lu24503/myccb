/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.distributor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 申请分销员
 */
@Data
@ApiModel(value = "ShopDistributorParam", description = "申请分销员")
public class ShopDistributorParam {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String distributorName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String distributorPhone;

    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码")
    private String invitationCode;
}
