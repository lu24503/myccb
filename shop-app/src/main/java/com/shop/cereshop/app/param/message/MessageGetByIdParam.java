/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取短信配置列表请求
 */
@Data
@ApiModel(value = "MessageGetByIdParam", description = "获取短信配置列表请求")
public class MessageGetByIdParam {

    /**
     * 短信id
     */
    @ApiModelProperty(value = "短信id")
    private Long messageId;
}
