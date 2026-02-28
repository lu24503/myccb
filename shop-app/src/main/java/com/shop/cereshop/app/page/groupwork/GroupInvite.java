/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.groupwork;

import com.shop.cereshop.app.page.order.CollagePerson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 邀请好友拼单数据
 */
@Data
@ApiModel(value = "GroupInvite", description = "邀请好友拼单数据")
public class GroupInvite {

    /**
     * 邀请人头像
     */
    @ApiModelProperty(value = "邀请人头像")
    private String headImage;

    /**
     * 拼单id
     */
    @ApiModelProperty(value = "拼单id")
    private Long collageId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;

    /**
     * 商品原价
     */
    @ApiModelProperty(value = "商品原价")
    private BigDecimal originalPrice;

    /**
     * 商品售价（拼团价）
     */
    @ApiModelProperty(value = "商品售价（拼团价）")
    private BigDecimal price;

    /**
     * 成团人数
     */
    @ApiModelProperty(value = "成团人数")
    private Integer person;

    /**
     * 参与拼团人员数据
     */
    @ApiModelProperty(value = "参与拼团人员数据")
    private List<CollagePerson> personList;

    /**
     * 活动结束倒计时
     */
    @ApiModelProperty(value = "活动结束倒计时")
    private long time;

    /**
     * 活动状态
     */
    @ApiModelProperty(value = "活动状态")
    private Integer activityState;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private Integer orderState;

    /**
     * 拼单状态
     */
    @ApiModelProperty(value = "拼单状态")
    private Integer collageOrderState;
}
