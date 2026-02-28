/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.member;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员专区分页商品请求参数
 */
@Data
@ApiModel(value = "MemberProductPageParam", description = "会员专区分页商品请求参数")
public class MemberProductPageParam extends PageParam {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 搜索字段
     */
    @ApiModelProperty(value = "搜索字段")
    private String search;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long classifyId;

    @ApiModelProperty(value = "分类层级")
    private Integer classifyLevel;

    /**
     * 价格排序条件 1-升序 2-降序
     */
    @ApiModelProperty(value = "价格排序条件 1-升序 2-降序")
    private Integer type;

    /**
     * 销量排序条件 1-升序 2-降序
     */
    @ApiModelProperty(value = "销量排序条件 1-升序 2-降序")
    private Integer volume;

    /**
     * 会员等级id
     */
    private Long memberLevelId;

}
