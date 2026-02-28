/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品评论数据
 */
@Data
@ApiModel(value = "BuyerComment", description = "商品评论数据")
public class BuyerComment {

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long commentId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String shopName;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String name;

    /**
     * 客户头像
     */
    @ApiModelProperty(value = "客户头像")
    private String headImage;

    /**
     * 图片数据
     */
    private String image;

    /**
     * 追加图片数据
     */
    private String addImage;

    /**
     * 图片数据数组
     */
    @ApiModelProperty(value = "图片数据")
    private List<String> images;

    /**
     * 追加图片数组
     */
    @ApiModelProperty(value = "图片数据")
    private List<String> addImages;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    private String comment;

    /**
     * 追加评论内容
     */
    @ApiModelProperty(value = "追加评论内容")
    private String addComment;

    /**
     * 评论时间
     */
    @ApiModelProperty(value = "评论时间")
    private String createTime;

    /**
     * 规格值
     */
    private String value;

    /**
     * 规格值数组
     */
    @ApiModelProperty(value = "规格值数组")
    private List<String> values;

    /**
     * 是否点赞 1-是 0-否
     */
    @ApiModelProperty(value = "是否点赞 1-是 0-否")
    private Integer ifLike=0;

    /**
     * 点赞人数
     */
    @ApiModelProperty(value = "点赞人数")
    private Integer likes;

}
