/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.order.impl;

import com.shop.cereshop.app.dao.order.CereOrderProductAttributeDAO;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.order.OrderProductAttribute;
import com.shop.cereshop.app.service.order.CereOrderProductAttributeService;
import com.shop.cereshop.commons.domain.order.CereOrderProductAttribute;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereOrderProductAttributeServiceImpl implements CereOrderProductAttributeService {

    @Autowired
    private CereOrderProductAttributeDAO cereOrderProductAttributeDAO;

    @Override
    public List<CereOrderProductAttribute> findBySkuId(Long skuId) {
        return cereOrderProductAttributeDAO.findBySkuId(skuId);
    }

    @Override
    public void insertBatch(List<OrderProductAttribute> attributes) throws CoBusinessException {
        cereOrderProductAttributeDAO.insertBatch(attributes);
    }

    @Override
    public List<OrderProductAttribute> findBySkus(List<CartSku> skus) {
        return cereOrderProductAttributeDAO.findBySkus(skus);
    }
}
