/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.compose;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CereShopComposeDTO {

    private Long shopId;

    private Long composeId;

    private String composeName;

    private Integer composeType;

    private BigDecimal promote;

    private Long productId;
}
