/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单调取微信支付返回数据
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "OrderPay", description = "订单支付返回数据")
public class OrderPay {

    /**
     * appid
     */
    @ApiModelProperty(value = "appid")
    private String appId;

    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    private String timeStamp;

    /**
     * nonceStr
     */
    @ApiModelProperty(value = "nonceStr")
    private String nonceStr;

    /**
     * package
     */
    @ApiModelProperty(value = "package")
    @JsonProperty("package")
    private String packages;

    /**
     * 签名类型
     */
    @ApiModelProperty(value = "签名类型")
    private String signType;

    /**
     * 签名
     */
    @ApiModelProperty(value = "签名")
    private String paySign;

    /**
     * 收款码url
     */
    @ApiModelProperty(value = "收款码url")
    private String codeUrl;

    /**
     * h5支付链接
     */
    @ApiModelProperty(value = "h5支付链接")
    private String mwebUrl;

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号")
    private String partnerId;

    /**
     * 支付宝小程序支付-创建的订单id
     */
    @ApiModelProperty(value = "支付宝小程序支付-创建的订单id")
    private String tradeNo;

    /**
     * 预支付id
     */
    @ApiModelProperty(value = "预支付id")
    private String prepayId;
}
