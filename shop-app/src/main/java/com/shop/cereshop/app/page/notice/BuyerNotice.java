/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.notice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息数据
 */
@Data
@ApiModel(value = "BuyerNotice", description = "消息数据")
public class BuyerNotice {

    /**
     * 消息id
     */
    @ApiModelProperty(value = "消息id")
    private Long noticeId;

    /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题")
    private String noticeTitle;

    /**
     * 消息类型 消息类型 1-系统消息（系统自动发送） 2-公告（后台录入） 3-站内信
     */
    @ApiModelProperty(value = "消息类型 1-系统消息（系统自动发送） 2-公告（后台录入） 3-站内信")
    private Integer noticeType;

    /**
     * 跳转类型 1-商品详情 2-订单详情
     */
    @ApiModelProperty(value = "跳转类型 1-商品详情 2-订单详情")
    private Integer jump;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String noticeContent;

    /**
     * 消息产生时间
     */
    @ApiModelProperty(value = "消息产生时间")
    private String createTime;

    /**
     * 点击查看详情唯一标识
     */
    @ApiModelProperty(value = "点击查看详情唯一标识")
    private Long only;

    /**
     * 是否读取 1-是 0-否
     */
    @ApiModelProperty(value = "是否已读")
    private Integer ifRead;
}
