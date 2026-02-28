/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.stock;

import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CereStockService {

    ProductStockInfo getActivityProductStock(Integer activityType, Long productId);

    ProductStockInfo getActivitySkuStock(Integer activityType, Long skuId);

    ProductStockInfo getSkuStockInfo(Long skuId);

    void updateActivityProductStock(Integer activityType, Long productId, Integer total, Integer stockNumber);

    void updateActivitySkuStock(Integer activityType, Long skuId, Integer total, Integer stockNumber);

    void updateSkuStock(Long skuId, Integer total, Integer stockNumber);

    void reduceStock(Integer activityType, Long activityId, Long productId, Long skuId, Integer buyNumber, Integer ifOversold) throws CoBusinessException;

    void rollbackStock(Integer activityType, Long activityId, Long productId, Long skuId, Integer buyNumber);

    void rollbackStockByOrderId(Long orderId);

    /**
     * 查询商家秒杀/商家折扣 对应sku的活动库存
     * @param activityType
     * @param activityId
     * @param skuId
     * @return
     */
    ProductStockInfo getOrQueryShopActivitySkuStock(Integer activityType, Long activityId, Long skuId);
}
