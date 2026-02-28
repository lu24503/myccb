/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 参与拼团人员信息
 */
@Data
@ApiModel(value = "CollagePerson", description = "参与拼团人员信息")
public class CollagePerson {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long buyerUserId;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String headImage;
}
