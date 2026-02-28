/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.groupwork;

import com.shop.cereshop.app.domain.activity.ActivityData;
import com.shop.cereshop.app.page.tool.ToolProduct;

import java.math.BigDecimal;
import java.util.List;

public interface CereShopGroupWorkDetailService {

    BigDecimal findPriceByGroupWorkIdAndSkuId(Long shopGroupWorkId, Long skuId);

    ActivityData findPriceBySkuId(Long skuId);

    List<ToolProduct> findDistinctProducts(Long shopGroupWorkId);

    Long findSkuIdByProductId(Long productId);
}
