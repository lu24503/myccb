/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 上传文件返回地址
 */
@Data
@ApiModel(value = "Url", description = "上传文件返回地址")
@AllArgsConstructor
public class Url {

    /**
     * 文件地址
     */
    @ApiModelProperty(value = "文件地址")
    private String url;
}
