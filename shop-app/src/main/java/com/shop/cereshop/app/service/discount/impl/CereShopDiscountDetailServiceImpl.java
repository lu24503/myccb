/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.discount.impl;

import com.shop.cereshop.app.dao.discount.CereShopDiscountDetailDAO;
import com.shop.cereshop.app.domain.activity.ActivityData;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.service.discount.CereShopDiscountDetailService;
import com.shop.cereshop.commons.domain.tool.CereShopDiscountDetail;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CereShopDiscountDetailServiceImpl implements CereShopDiscountDetailService {

    @Autowired
    private CereShopDiscountDetailDAO cereShopDiscountDetailDAO;

    @Override
    public CereShopDiscountDetail findSkuDetail(Long shopDiscountId, Long orderId) {
        return cereShopDiscountDetailDAO.findSkuDetail(shopDiscountId,orderId);
    }

    @Override
    public int updateNumber(CereShopDiscountDetail detail) throws CoBusinessException {
        return cereShopDiscountDetailDAO.updateNumber(detail);
    }

    @Override
    public ActivityData findPriceBySkuId(Long skuId) {
        return cereShopDiscountDetailDAO.findPriceBySkuId(skuId);
    }

    @Override
    public List<ToolProduct> findDistinctProducts(Long shopDiscountId) {
        return cereShopDiscountDetailDAO.findDistinctProducts(shopDiscountId);
    }

    @Override
    public int findNumber(Long shopDiscountId, Long skuId) {
        return cereShopDiscountDetailDAO.findNumber(shopDiscountId,skuId);
    }

    @Override
    public void updateBatch(List<CereShopDiscountDetail> discountDetails) throws CoBusinessException {
        cereShopDiscountDetailDAO.updateBatch(discountDetails);
    }

    @Override
    public List<CereShopDiscountDetail> findNumberDetails(Long orderId, Long shopDiscountId) {
        return cereShopDiscountDetailDAO.findNumberDetails(orderId,shopDiscountId);
    }

    @Override
    public ProductStockInfo selectSkuStockInfo(Long shopDiscountId, Long skuId) {
        return cereShopDiscountDetailDAO.selectSkuStockInfo(shopDiscountId, skuId);
    }

    @Override
    public BigDecimal findPriceByDiscountIdAndSkuId(Long shopDiscountId, Long skuId) {
        return cereShopDiscountDetailDAO.findPriceByDiscountIdAndSkuId(shopDiscountId, skuId);
    }

    @Override
    public Long findSkuIdByProductId(Long productId) {
        return cereShopDiscountDetailDAO.findSkuIdByProductId(productId);
    }

    @Override
    public int rollbackStock(Long discountId, Long skuId, Integer buyNumber) {
        return cereShopDiscountDetailDAO.rollbackStock(discountId, skuId, buyNumber);
    }
}
