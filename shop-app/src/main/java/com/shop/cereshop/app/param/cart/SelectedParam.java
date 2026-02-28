/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.cart;

import com.shop.cereshop.app.page.cart.ShopCart;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 选中商品请求
 */
@Data
@ApiModel(value = "SelectedParam", description = "选中商品请求")
public class SelectedParam {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private List<ShopCart> shopCarts;
}
