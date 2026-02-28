/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.domain.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("商品sku对应的规格编码和规格值编码")
public class SkuAttrCodeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("sku对应的规格编码")
    private String attrCode;

    @ApiModelProperty("sku对应的规格值编码")
    private String attrValueCode;

}
