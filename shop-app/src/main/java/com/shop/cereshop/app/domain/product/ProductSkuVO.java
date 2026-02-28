/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.domain.product;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("商品sku的前端返回对象")
@Data
public class ProductSkuVO extends ProductSkuSaveDTO {
    private static final long serialVersionUID = 1L;
}
