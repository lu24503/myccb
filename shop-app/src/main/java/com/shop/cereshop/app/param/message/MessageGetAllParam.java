/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.message;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取短信配置详情请求
 */
@Data
@ApiModel(value = "MessageGetAllParam", description = "获取短信配置详情请求")
public class MessageGetAllParam extends PageParam {

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
}
