/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.shop;

import com.shop.cereshop.commons.domain.shop.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺信息返回数据实体类
 */
@Data
@ApiModel(value = "Shop", description = "店铺信息返回数据实体类")
public class PlatformShop {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺logo")
    private String shopLogo;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 店铺地址
     */
    @ApiModelProperty(value = "店铺地址")
    private String shopAdress;

    /**
     * 店铺简介
     */
    @ApiModelProperty(value = "店铺简介")
    private String shopBrief;

    /**
     * 店铺手机号
     */
    @ApiModelProperty(value = "店铺手机号")
    private String shopPhone;

    /**
     * 主体类型
     */
    @ApiModelProperty(value = "主体类型")
    private Integer authenType;

    /**
     * 认证状态  1-立即认证 2-认证详情 3-立即签约 4-查看详情 5-查看详情
     */
    @ApiModelProperty(value = "认证状态  1-立即认证 2-认证详情 3-立即签约 4-查看详情 5-查看详情")
    private Integer authenticationState;

    /**
     * 退货地址信息
     */
    @ApiModelProperty(value = "退货地址信息")
    private CereShopReturn shopReturn;

    /**
     * 个人信息
     */
    @ApiModelProperty(value = "个人信息")
    private CereShopPersonal personal;

    /**
     * 个体工商户信息
     */
    @ApiModelProperty(value = "个体工商户信息")
    private CereShopIndividualBusinesses individualBusinesses;

    /**
     * 企业信息
     */
    @ApiModelProperty(value = "企业信息")
    private CereShopEnterprise enterprise;

    /**
     * 其他组织信息
     */
    @ApiModelProperty(value = "其他组织信息")
    private CereShopOtherOrganizations otherOrganizations;
}
