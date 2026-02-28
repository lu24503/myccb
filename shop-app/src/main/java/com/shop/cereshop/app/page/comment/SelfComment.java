/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 我的评论数据
 */
@Data
@ApiModel(value = "BuyerComment", description = "商品评论数据")
public class SelfComment {

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long commentId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 商品售价
     */
    @ApiModelProperty(value = "商品售价")
    private String productPrice;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String productImage;

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
     * 追评时间
     */
    private String addTime;

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
    private Integer ifLike;

    /**
     * 点赞人数
     */
    @ApiModelProperty(value = "点赞人数")
    private Integer likes;

    /**
     * 是否追评 1-是 0-否
     */
    @ApiModelProperty(value = "是否追评 1-是 0-否")
    private Integer ifAdd;

    /**
     * 商品满意度(宝贝描述) 1-一星 2-二星 3-三星 4-四星 5-五星
     */
    @ApiModelProperty(value = "商品满意度(宝贝描述) 1-一星 2-二星 3-三星 4-四星 5-五星")
    private Integer star;

    /**
     * 描述相符 1-一星 2-二星 3-三星 4-四星 5-五星
     */
    @ApiModelProperty(value = "描述相符 1-一星 2-二星 3-三星 4-四星 5-五星")
    private Integer des;

    /**
     * 物流服务 1-一星 2-二星 3-三星 4-四星 5-五星
     */
    @ApiModelProperty(value = "物流服务 1-一星 2-二星 3-三星 4-四星 5-五星")
    private Integer delivery;

    /**
     * 服务态度 1-一星 2-二星 3-三星 4-四星 5-五星
     */
    @ApiModelProperty(value = "服务态度 1-一星 2-二星 3-三星 4-四星 5-五星")
    private Integer attitude;

    /**
     * 商品印象
     */
    @ApiModelProperty(value = "商品印象")
    private String impression;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺logo")
    private String shopLogo;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private String productId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private String skuId;

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long orderId;
}
