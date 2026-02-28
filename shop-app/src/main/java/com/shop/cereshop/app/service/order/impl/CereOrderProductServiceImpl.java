/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.order.impl;

import com.shop.cereshop.app.dao.order.CereOrderProductDAO;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.service.order.CereOrderProductService;
import com.shop.cereshop.commons.domain.order.CereOrderProduct;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereOrderProductServiceImpl implements CereOrderProductService {

    @Autowired
    private CereOrderProductDAO cereOrderProductDAO;

    @Override
    public void insert(CereOrderProduct orderProduct) throws CoBusinessException {
        cereOrderProductDAO.insert(orderProduct);
    }

    @Override
    public List<CartSku> findOrderProductSku(Long orderId) {
        return cereOrderProductDAO.findOrderProductSku(orderId);
    }

    @Override
    public List<CereOrderProduct> findByOrderIds(List<Long> orderIdList) {
        return cereOrderProductDAO.findByOrderIds(orderIdList);
    }

    @Override
    public List<CartSku> findProductStatsByOrderIds(List<Long> orderIdList) {
        return cereOrderProductDAO.findProductStatsByOrderIds(orderIdList);
    }
}
