/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.discount;

import com.shop.cereshop.app.domain.activity.ActivityData;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.commons.domain.tool.CereShopDiscountDetail;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.math.BigDecimal;
import java.util.List;

public interface CereShopDiscountDetailService {
    CereShopDiscountDetail findSkuDetail(Long shopDiscountId, Long orderId);

    int updateNumber(CereShopDiscountDetail detail) throws CoBusinessException;

    ActivityData findPriceBySkuId(Long skuId);

    List<ToolProduct> findDistinctProducts(Long shopDiscountId);

    int findNumber(Long shopDiscountId, Long skuId);

    void updateBatch(List<CereShopDiscountDetail> discountDetails) throws CoBusinessException;

    List<CereShopDiscountDetail> findNumberDetails(Long orderId, Long shopDiscountId);

    ProductStockInfo selectSkuStockInfo(Long shopDiscountId, Long skuId);

    BigDecimal findPriceByDiscountIdAndSkuId(Long shopDiscountId, Long skuId);

    Long findSkuIdByProductId(Long productId);

    int rollbackStock(Long discountId, Long skuId, Integer buyNumber);
}
