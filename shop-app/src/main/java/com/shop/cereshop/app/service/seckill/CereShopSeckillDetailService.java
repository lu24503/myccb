/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.seckill;

import com.shop.cereshop.app.domain.activity.ActivityData;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.commons.domain.tool.CereShopSeckillDetail;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.math.BigDecimal;
import java.util.List;

public interface CereShopSeckillDetailService {
    CereShopSeckillDetail findSkuDetail(Long shopSeckillId, Long orderId);

    int updateNumber(CereShopSeckillDetail detail) throws CoBusinessException;

    ActivityData findPriceBySkuId(Long skuId);

    List<ToolProduct> findDistinctProducts(Long shopSeckillId);

    int findNumber(Long shopSeckillId, Long skuId);

    void updateBatch(List<CereShopSeckillDetail> seckillDetails) throws CoBusinessException;

    List<CereShopSeckillDetail> findNumberDetails(Long orderId, Long shopSeckillId);

    ProductStockInfo selectSkuStockInfo(Long shopSeckillId, Long skuId);

    BigDecimal findPriceBySeckillIdAndSkuId(Long shopSeckillId, Long skuId);

    Long findSkuIdByProductId(Long productId);

    int rollbackStock(Long seckillId, Long skuId, Integer buyNumber);
}
