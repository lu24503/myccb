/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.after;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 售后协商历史返回数据实体
 */
@Data
@ApiModel(value = "AfterHistory", description = "售后协商历史返回数据实体")
public class AfterHistory {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String reason;

    /**
     * 操作人姓名
     */
    @ApiModelProperty(value = "操作人姓名")
    private String name;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private String time;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private List<String> images;

}
