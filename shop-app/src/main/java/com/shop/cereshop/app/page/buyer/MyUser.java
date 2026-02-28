/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.buyer;

import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的信息
 */
@Data
@ApiModel(value = "MyUser", description = "我的信息")
public class MyUser extends CereBuyerUser {

    /**
     * 未读取消息总数
     */
    @ApiModelProperty(value = "未读取消息总数")
    private Integer notRead;

    /**
     * 未读回复数
     */
    @ApiModelProperty(value = "未读回复数")
    private Integer unreadReply;

    /**
     * 待付款订单数
     */
    @ApiModelProperty(value = "待付款订单数")
    private Integer waitPayOrderCount;

    /**
     * 待发货订单数
     */
    @ApiModelProperty(value = "待发货订单数")
    private Integer waitSendOrderCount;

    /**
     * 待收货订单数
     */
    @ApiModelProperty(value = "待收货订单数")
    private Integer waitReceiveOrderCount;

    /**
     * 总订单数
     */
    @ApiModelProperty(value = "总订单数")
    private Integer totalOrderCount;


    /**
     * 累计积分
     */
    @ApiModelProperty(value = "累计积分")
    private Integer totalCredit;

    /**
     * 售后数量
     */
    @ApiModelProperty(value = "售后数量")
    private Integer afterCount;

    /**
     * 收藏数量
     */
    @ApiModelProperty(value = "收藏数量")
    private Integer collectCount;

    /**
     * 问答数量
     */
    @ApiModelProperty(value = "问答数量")
    private Integer askAnswerCount;

    /**
     * 评价数量
     */
    @ApiModelProperty(value = "评价数量")
    private Integer commentCount;

    /**
     * 足迹数量
     */
    @ApiModelProperty(value = "足迹数量")
    private Integer visitCount;
}
