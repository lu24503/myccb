/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 手机号授权
 */
@Data
@ApiModel(value = "LoginPhoneParam", description = "手机号授权")
public class LoginPhoneParam {

    /**
     * 微信openID
     */
    @ApiModelProperty(value = "微信openID")
    private String wechatOpenId;

    /**
     * 加密秘钥
     */
    @ApiModelProperty(value = "加密秘钥")
    private String sessionKey;

    /**
     * 加密数据
     */
    @ApiModelProperty(value = "加密数据")
    private String encryptedData;

    /**
     * 加密算法初始向量
     */
    @ApiModelProperty(value = "加密算法初始向量")
    private String iv;

    /**
     * 微信头像
     */
    @ApiModelProperty(value = "微信头像")
    private String headImage;

    /**
     * 登录ip
     */
    private String ip;

    /**
     * 渠道编码
     */
    private String channelCode;
}
