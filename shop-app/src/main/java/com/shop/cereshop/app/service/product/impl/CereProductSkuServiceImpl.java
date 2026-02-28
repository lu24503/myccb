/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.product.impl;

import com.shop.cereshop.app.dao.product.CereProductSkuDAO;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.product.ProductSkus;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.product.SkuTool;
import com.shop.cereshop.app.param.groupwork.GroupWorkSettlementParam;
import com.shop.cereshop.app.param.settlement.ProductSku;
import com.shop.cereshop.app.service.product.CereProductSkuService;
import com.shop.cereshop.commons.domain.product.CereProductSku;
import com.shop.cereshop.commons.domain.product.Sku;
import com.shop.cereshop.commons.domain.product.SkuName;
import com.shop.cereshop.commons.domain.product.SkuValue;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CereProductSkuServiceImpl implements CereProductSkuService {

    @Autowired
    private CereProductSkuDAO cereProductSkuDAO;

    @Override
    public List<SkuName> findSkuNames(Long productId) {
        return cereProductSkuDAO.findSkuNames(productId);
    }

    @Override
    public List<SkuValue> findSkuValues(String skuName, Long productId) {
        return cereProductSkuDAO.findSkuValues(skuName,productId);
    }

    @Override
    public ProductSkus findSku(Long skuId) {
        return cereProductSkuDAO.findSku(skuId);
    }

    @Override
    public ProductSkus findFirstSku(Long productId) {
        return cereProductSkuDAO.findFirstSku(productId);
    }

    @Override
    public List<Sku> findSimpleSkuByProductId(Long productId) {
        return cereProductSkuDAO.findSimpleSkuByProductId(productId);
    }

    @Override
    public List<Sku> findSkuByProductId(Long productId) {
        return cereProductSkuDAO.findSkuByProductId(productId);
    }

    @Override
    public String findValueBySkuId(Long skuId) {
        return cereProductSkuDAO.findValueBySkuId(skuId);
    }

    @Override
    public Integer findStockNumberBySkuId(Long skuId) {
        return cereProductSkuDAO.findStockNumberBySkuId(skuId);
    }

    @Override
    public Integer findStockNumber(Long skuId) {
        return cereProductSkuDAO.findStockNumber(skuId);
    }

    @Override
    public List<CartSku> findStockNumberBySkus(List<ProductSku> skus) {
        return cereProductSkuDAO.findStockNumberBySkus(skus);
    }

    @Override
    public void updateBatch(List<CartSku> productSkus) throws CoBusinessException {
        cereProductSkuDAO.updateBatch(productSkus);
    }

    @Override
    public List<CartSku> findSkuBySkus(List<ProductSku> skus) {
        return cereProductSkuDAO.findSkuBySkus(skus);
    }

    @Override
    public List<CartSku> findSkuByOrderId(Long orderId) {
        return cereProductSkuDAO.findSkuByOrderId(orderId);
    }

    @Override
    public List<CartSku> findSkuByIds(List<Long> ids,Long orderId) {
        return cereProductSkuDAO.findSkuByIds(ids,orderId);
    }

    @Override
    public List<CereProductSku> findStockNumberByOrderId(Long orderId) {
        return cereProductSkuDAO.findStockNumberByOrderId(orderId);
    }

    @Override
    public List<CereProductSku> findAll() {
        return cereProductSkuDAO.findAll();
    }

    @Override
    public List<SkuValue> findOneValues(Long skuId) {
        return cereProductSkuDAO.findOneValues(skuId);
    }

    @Override
    public List<Sku> findGroupWorkSkuByProductId(Long productId, Long shopGroupWorkId) {
        return cereProductSkuDAO.findGroupWorkSkuByProductId(productId,shopGroupWorkId);
    }

    @Override
    public List<CartSku> findSkuBySkuId(Long skuId) {
        return cereProductSkuDAO.findSkuBySkuId(skuId);
    }

    @Override
    public List<Sku> findSeckillSkuByProductId(Long productId, Long shopSeckillId) {
        return cereProductSkuDAO.findSeckillSkuByProductId(productId,shopSeckillId);
    }

    @Override
    public List<Sku> findDiscountSkuByProductId(Long productId, Long shopDiscountId) {
        return cereProductSkuDAO.findDiscountSkuByProductId(productId,shopDiscountId);
    }

    @Override
    public List<CartSku> findSkuBySkuIdAndWorkId(GroupWorkSettlementParam param) {
        return cereProductSkuDAO.findSkuBySkuIdAndWorkId(param);
    }

    @Override
    public List<SkuValue> findValuesByProductId(Long productId) {
        return cereProductSkuDAO.findValuesByProductId(productId);
    }

    @Override
    public List<CartSku> findOriginalSkuBySkus(List<ProductSku> skus) {
        return cereProductSkuDAO.findOriginalSkuBySkus(skus);
    }

    @Override
    public List<CartSku> findGroupWorkStockNumberBySkus(List<ProductSku> skus,Long shopGroupWorkId) {
        return cereProductSkuDAO.findGroupWorkStockNumberBySkus(skus,shopGroupWorkId);
    }

    @Override
    public List<CartSku> findSeckillStockNumberBySkus(List<ProductSku> skus,Long shopSeckillId) {
        return cereProductSkuDAO.findSeckillStockNumberBySkus(skus,shopSeckillId);
    }

    @Override
    public List<CartSku> findDiscountStockNumberBySkus(List<ProductSku> skus,Long shopDiscountId) {
        return cereProductSkuDAO.findDiscountStockNumberBySkus(skus,shopDiscountId);
    }

    @Override
    public void updateBatchSkus(List<CereProductSku> productSkus) throws CoBusinessException {
        cereProductSkuDAO.updateBatchSkus(productSkus);
    }

    @Override
    public SkuTool findTool(Long productId) {
        return cereProductSkuDAO.findTool(productId);
    }

    @Override
    public ProductStockInfo findProductStockInfo(Long productId) {
        return cereProductSkuDAO.findProductStockInfo(productId);
    }

    @Override
    public int updateStockNumber(Long skuId, int stockNumber) {
        CereProductSku sku = new CereProductSku();
        sku.setSkuId(skuId);
        sku.setStockNumber(stockNumber);
        return cereProductSkuDAO.updateByPrimaryKeySelective(sku);
    }

    @Override
    public int rollbackStock(Long skuId, Integer buyNumber) {
        return cereProductSkuDAO.rollbackStock(skuId, buyNumber);
    }

    @Override
    public List<SkuValue> findSkuValueBySkuId(String skuName, Long skuId) {
        return cereProductSkuDAO.findSkuValueBySkuId(skuName, skuId);
    }

    @Override
    public List<Sku> findStockNumberByProductId(Long productId) {
        return cereProductSkuDAO.findStockNumberByProductId(productId);
    }

    @Override
    public List<CartSku> findStockNumberBySkusForRealInfo(List<ProductSku> skus) {
        List<Long> skuIdList = skus.stream().map(ProductSku::getSkuId).collect(Collectors.toList());
        return cereProductSkuDAO.findCartSkuList(skuIdList);
    }

}
