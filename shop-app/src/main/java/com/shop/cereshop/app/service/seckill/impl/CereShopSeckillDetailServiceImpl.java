/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.seckill.impl;

import com.shop.cereshop.app.dao.seckill.CereShopSeckillDetailDAO;
import com.shop.cereshop.app.domain.activity.ActivityData;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.service.seckill.CereShopSeckillDetailService;
import com.shop.cereshop.commons.domain.tool.CereShopSeckillDetail;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CereShopSeckillDetailServiceImpl implements CereShopSeckillDetailService {

    @Autowired
    private CereShopSeckillDetailDAO cereShopSeckillDetailDAO;

    @Override
    public CereShopSeckillDetail findSkuDetail(Long shopSeckillId, Long orderId) {
        return cereShopSeckillDetailDAO.findSkuDetail(shopSeckillId,orderId);
    }

    @Override
    public int updateNumber(CereShopSeckillDetail detail) throws CoBusinessException {
        return cereShopSeckillDetailDAO.updateNumber(detail);
    }

    @Override
    public ActivityData findPriceBySkuId(Long skuId) {
        return cereShopSeckillDetailDAO.findPriceBySkuId(skuId);
    }

    @Override
    public List<ToolProduct> findDistinctProducts(Long shopSeckillId) {
        return cereShopSeckillDetailDAO.findDistinctProducts(shopSeckillId);
    }

    @Override
    public int findNumber(Long shopSeckillId, Long skuId) {
        return cereShopSeckillDetailDAO.findNumber(shopSeckillId,skuId);
    }

    @Override
    public void updateBatch(List<CereShopSeckillDetail> seckillDetails) throws CoBusinessException {
        cereShopSeckillDetailDAO.updateBatch(seckillDetails);
    }

    @Override
    public List<CereShopSeckillDetail> findNumberDetails(Long orderId, Long shopSeckillId) {
        return cereShopSeckillDetailDAO.findNumberDetails(orderId,shopSeckillId);
    }

    @Override
    public ProductStockInfo selectSkuStockInfo(Long shopSeckillId, Long skuId) {
        return cereShopSeckillDetailDAO.selectSkuStockInfo(shopSeckillId, skuId);
    }

    @Override
    public BigDecimal findPriceBySeckillIdAndSkuId(Long shopSeckillId, Long skuId) {
        return cereShopSeckillDetailDAO.findPriceBySeckillIdAndSkuId(shopSeckillId, skuId);
    }

    @Override
    public Long findSkuIdByProductId(Long productId) {
        return cereShopSeckillDetailDAO.findSkuIdByProductId(productId);
    }

    @Override
    public int rollbackStock(Long seckillId, Long skuId, Integer buyNumber) {
        return cereShopSeckillDetailDAO.rollbackStock(seckillId, skuId, buyNumber);
    }
}
