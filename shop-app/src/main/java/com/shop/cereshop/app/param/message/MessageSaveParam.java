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
 * 添加短信配置请求
 */
@Data
@ApiModel(value = "MessageSaveParam", description = "添加短信配置请求")
public class MessageSaveParam {

    /**
     * 第三方短信名称
     */
    @ApiModelProperty(value = "第三方短信名称")
    private String messageProject;

    /**
     * 短信模板名称
     */
    @ApiModelProperty(value = "短信模板名称")
    private String messageTemplate;

    /**
     * 模板编码
     */
    @ApiModelProperty(value = "模板编码")
    private String templateCode;

    /**
     * 模板签名
     */
    @ApiModelProperty(value = "模板签名")
    private String templateSign;

    /**
     * 模板描述
     */
    @ApiModelProperty(value = "模板描述")
    private String templateDescribe;
}
