/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 推广数据
 */
@Data
@ApiModel(value = "Extension", description = "推广数据")
public class Extension {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 分销员id
     */
    @ApiModelProperty(value = "分销员id")
    private Long distributorId;

    /**
     * 是否展示头像昵称 1-是 0-否
     */
    @ApiModelProperty(value = "是否展示头像昵称 1-是 0-否")
    private Integer ifHead;

    /**
     * 店铺logo是否展示在二维码 1-是 0-否
     */
    @ApiModelProperty(value = "店铺logo是否展示在二维码 1-是 0-否")
    private Integer ifLogo;

    /**
     * 推广语
     */
    @ApiModelProperty(value = "推广语")
    private String extensionReason;

    /**
     * 背景图片地址 875x1275像素
     */
    @ApiModelProperty(value = "背景图片地址 875x1275像素")
    private String image;

    /**
     * 二维码
     */
    @ApiModelProperty(value = "二维码")
    private String poster;

    /**
     * 客户姓名
     */
    @ApiModelProperty(value = "客户姓名")
    private String name;

    /**
     * 客户头像
     */
    @ApiModelProperty(value = "客户头像")
    private String headImage;

    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码")
    private String InvitationCode;
}
