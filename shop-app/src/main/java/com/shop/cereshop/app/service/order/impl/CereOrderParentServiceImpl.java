/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.order.impl;

import com.shop.cereshop.app.dao.order.CereOrderParentDAO;
import com.shop.cereshop.app.service.order.CereOrderParentService;
import com.shop.cereshop.commons.domain.order.CereOrderParent;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereOrderParentServiceImpl implements CereOrderParentService {

    @Autowired
    private CereOrderParentDAO cereOrderParentDAO;

    @Override
    public void insert(CereOrderParent parent) throws CoBusinessException {
        cereOrderParentDAO.insert(parent);
    }

    @Override
    public CereOrderParent findById(Long id) {
        return cereOrderParentDAO.findById(id);
    }

    @Override
    public List<Long> findShopIds(Long id) {
        return cereOrderParentDAO.findShopIds(id);
    }
}
