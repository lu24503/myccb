/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.shop;

import com.shop.cereshop.commons.domain.shop.CereShopIndividualBusinesses;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 入驻申请个体工商户请求
 */
@Data
@ApiModel(value = "CereShopIndividualBusinessesParam", description = "入驻申请个体工商户请求")
public class CereShopIndividualBusinessesParam extends CereShopIndividualBusinesses {

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String shopPhone;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    @NotBlank(message = "店铺名称不能为空")
    private String shopName;

    /**
     * 店铺负责人
     */
    @ApiModelProperty(value = "店铺负责人")
    @NotBlank(message = "店铺负责人不能为空")
    private String chargePersonName;

    /**
     * 负责人电话
     */
    @ApiModelProperty(value = "负责人电话")
    @NotBlank(message = "负责人电话不能为空")
    private String chargePersonPhone;

    /**
     * 店铺地址
     */
    @ApiModelProperty(value = "店铺地址")
    @NotBlank(message = "店铺地址不能为空")
    private String shopAdress;
}
