/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.domain.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("商详店铺信息")
public class StoreDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺id")
    private Long id;

    private String storeName;

    @ApiModelProperty("店铺图像")
    private String logo;

    @ApiModelProperty("商品总类")
    private Integer productCount;

    @ApiModelProperty("已售数量")
    private Integer soldNum;
}
