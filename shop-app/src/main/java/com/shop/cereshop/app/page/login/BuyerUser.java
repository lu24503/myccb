/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录返回数据
 */
@Data
@ApiModel(value = "BuyerUser", description = "登录返回数据")
public class BuyerUser {
    /**
     * 微信昵称
     */
    @ApiModelProperty(value = "微信昵称")
    private String wechatName;

    /**
     * 微信openid
     */
    @ApiModelProperty(value = "微信openid")
    private String wechatOpenId;

    /**
     * 绑定手机号
     */
    @ApiModelProperty(value = "绑定手机号")
    private String phone;

    /**
     * 请求token
     */
    @ApiModelProperty(value = "请求token")
    private String token;

    /**
     * 刷新token
     */
    @ApiModelProperty(value = "刷新token")
    private String refreshToken;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Long buyerUserId;

    /**
     * 是否启用状态 1-是 0-否
     */
    @ApiModelProperty(value = "是否启用状态 1-是 0-否")
    private Integer state;

    /**
     * 是否加入黑名单 1-是 0-否
     */
    @ApiModelProperty(value = "是否加入黑名单 1-是 0-否")
    private Integer ifBlack;

    /**
     * 微信加密秘钥
     */
    @ApiModelProperty(value = "微信加密秘钥")
    private String sessionKey;

    /**
     * 是否第一次登录 1-是 0-否
     */
    @ApiModelProperty(value = "是否第一次登录 1-是 0-否")
    private Integer ifFirst;

    /**
     * 头像图片
     */
    @ApiModelProperty(value = "头像图片")
    private String headImage;

    /**
     * 支付宝小程序用户id
     */
    @ApiModelProperty(value = "支付宝小程序用户id")
    private String aliUserId;

    /**
     * 会员等级id
     */
    @ApiModelProperty(value = "会员等级id")
    private Long memberLevelId;

    /**
     * 会员等级名称
     */
    @ApiModelProperty(value = "会员等级名称")
    private String memberLevelName;

    /**
     * 成长值
     */
    @ApiModelProperty(value = "成长值")
    private Integer growth;

    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Integer credit;

    /**
     * 下一级需要的成长值
     */
    @ApiModelProperty(value = "下一级需要的成长值")
    private Integer nextLevelGrowth;

    /**
     * 下一级会员等级名称
     */
    private String nextLevelName;
}
