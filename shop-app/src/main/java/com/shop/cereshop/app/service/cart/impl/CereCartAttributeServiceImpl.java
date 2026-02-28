/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.cart.impl;

import com.shop.cereshop.app.dao.cart.CereCartAttributeDAO;
import com.shop.cereshop.app.service.cart.CereCartAttributeService;
import com.shop.cereshop.commons.domain.cart.CereCartAttribute;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereCartAttributeServiceImpl implements CereCartAttributeService {

    @Autowired
    private CereCartAttributeDAO cereCartAttributeDAO;

    @Override
    public void insertBatch(List<CereCartAttribute> attributes) throws CoBusinessException {
        cereCartAttributeDAO.insertBatch(attributes);
    }
}
