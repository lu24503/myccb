/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.product;

import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.product.ProductSkus;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.product.SkuTool;
import com.shop.cereshop.app.param.groupwork.GroupWorkSettlementParam;
import com.shop.cereshop.app.param.settlement.ProductSku;
import com.shop.cereshop.commons.domain.product.CereProductSku;
import com.shop.cereshop.commons.domain.product.Sku;
import com.shop.cereshop.commons.domain.product.SkuName;
import com.shop.cereshop.commons.domain.product.SkuValue;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereProductSkuService {
    List<SkuName> findSkuNames(Long productId);

    List<SkuValue> findSkuValues(String skuName, Long productId);

    ProductSkus findSku(Long skuId);

    ProductSkus findFirstSku(Long productId);

    List<Sku> findSimpleSkuByProductId(Long productId);

    List<Sku> findSkuByProductId(Long productId);

    String findValueBySkuId(Long skuId);

    Integer findStockNumberBySkuId(Long skuId);

    Integer findStockNumber(Long skuId);

    List<CartSku> findStockNumberBySkus(List<ProductSku> skus);

    void updateBatch(List<CartSku> productSkus) throws CoBusinessException;

    List<CartSku> findSkuBySkus(List<ProductSku> skus);

    List<CartSku> findSkuByOrderId(Long orderId);

    List<CartSku> findSkuByIds(List<Long> ids,Long orderId);

    List<CereProductSku> findStockNumberByOrderId(Long orderId);

    List<CereProductSku> findAll();

    List<SkuValue> findOneValues(Long skuId);

    List<Sku> findGroupWorkSkuByProductId(Long productId, Long shopGroupWorkId);

    List<CartSku> findSkuBySkuId(Long skuId);

    List<Sku> findSeckillSkuByProductId(Long productId, Long shopSeckillId);

    List<Sku> findDiscountSkuByProductId(Long productId, Long shopDiscountId);

    List<CartSku> findSkuBySkuIdAndWorkId(GroupWorkSettlementParam param);

    List<SkuValue> findValuesByProductId(Long productId);

    List<CartSku> findOriginalSkuBySkus(List<ProductSku> skus);

    List<CartSku> findGroupWorkStockNumberBySkus(List<ProductSku> skus,Long shopGroupWorkId);

    List<CartSku> findSeckillStockNumberBySkus(List<ProductSku> skus,Long shopSeckillId);

    List<CartSku> findDiscountStockNumberBySkus(List<ProductSku> skus,Long shopDiscountId);

    void updateBatchSkus(List<CereProductSku> productSkus) throws CoBusinessException;

    SkuTool findTool(Long productId);

    ProductStockInfo findProductStockInfo(Long productId);

    int updateStockNumber(Long skuId, int stockNumber);

    int rollbackStock(Long skuId, Integer buyNumber);

    List<SkuValue> findSkuValueBySkuId(String skuName, Long skuId);

    List<Sku> findStockNumberByProductId(Long productId);

    List<CartSku> findStockNumberBySkusForRealInfo(List<ProductSku> skus);
}
