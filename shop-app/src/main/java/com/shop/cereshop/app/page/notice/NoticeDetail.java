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
 * 公告详情
 */
@Data
@ApiModel(value = "NoticeDetail", description = "公告详情")
public class NoticeDetail {

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
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String noticeContent;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;

    /**
     * 消息产生时间
     */
    @ApiModelProperty(value = "消息产生时间")
    private String createTime;
}
