/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.after.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.cereshop.app.dao.after.CereAfterProductAttributeDAO;
import com.shop.cereshop.app.dao.after.CereAfterProductDAO;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.param.after.AfterParam;
import com.shop.cereshop.app.service.after.CereAfterProductService;
import com.shop.cereshop.commons.domain.after.CereAfterProduct;
import com.shop.cereshop.commons.domain.after.CereAfterProductAttribute;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CereAfterProductServiceImpl implements CereAfterProductService {

    @Autowired
    private CereAfterProductDAO cereAfterProductDAO;

    @Autowired
    private CereAfterProductAttributeDAO cereAfterProductAttributeDAO;

    @Override
    public void insert(CereAfterProduct afterProduct) throws CoBusinessException {
        cereAfterProductDAO.insert(afterProduct);
    }

    @Override
    public List<CereAfterProductAttribute> findValuesBySkuId(Long skuId) {
        return cereAfterProductDAO.findValuesBySkuId(skuId);
    }

    @Override
    public List<CartSku> findSkusByAfterId(Long afterId) {
        return cereAfterProductDAO.findSkusByAfterId(afterId);
    }

    @Override
    public List<CereAfterProduct> findSkuBySkus(AfterParam param) {
        return cereAfterProductDAO.findSkuBySkus(param);
    }

    @Override
    public void deleteByAfterId(Long id) {
        LambdaQueryWrapper<CereAfterProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CereAfterProduct::getAfterId, id);
        List<CereAfterProduct> afterProductList = cereAfterProductDAO.selectList(wrapper);

        if (CollectionUtils.isNotEmpty(afterProductList)) {
            cereAfterProductDAO.delete(wrapper);

            List<Long> afterProductIdList = afterProductList.stream().map(CereAfterProduct::getAfterProductId).collect(Collectors.toList());
            LambdaQueryWrapper<CereAfterProductAttribute> attributeWrapper = new LambdaQueryWrapper<>();
            attributeWrapper.in(CereAfterProductAttribute::getAfterProductId, afterProductIdList);
            cereAfterProductAttributeDAO.delete(attributeWrapper);
        }
    }

    @Override
    public List<CereAfterProduct> findByOrderIdForCheck(Long orderId) {
        return cereAfterProductDAO.findByOrderIdForCheck(orderId);
    }
}
