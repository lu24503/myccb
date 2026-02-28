/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.groupwork;

import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.utils.EmptyUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 拼团专区首页请求
 */
@Data
@ApiModel(value = "GroupWorkParam", description = "拼团专区首页请求")
public class GroupWorkParam extends PageParam {

    /**
     * 拼团活动id
     */
    @ApiModelProperty(value = "拼团活动id")
    private Long shopGroupWorkId;

    public void setShopGroupWorkId(Long shopGroupWorkId) {
        if(EmptyUtils.isLongEmpty(shopGroupWorkId)){
            this.shopGroupWorkId = null;
        }else {
            this.shopGroupWorkId = shopGroupWorkId;
        }
    }

    /**
     * 搜索字段
     */
    @ApiModelProperty(value = "搜索字段")
    private String search;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    public void setShopId(Long shopId) {
        if(EmptyUtils.isLongEmpty(shopId)){
            this.shopId=null;
        }else {
            this.shopId = shopId;
        }
    }

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
}
