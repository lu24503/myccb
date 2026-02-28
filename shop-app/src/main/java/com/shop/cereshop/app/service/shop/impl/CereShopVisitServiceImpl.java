/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop.impl;

import com.shop.cereshop.app.dao.shop.CereShopVisitDAO;
import com.shop.cereshop.app.service.shop.CereShopVisitService;
import com.shop.cereshop.commons.domain.shop.CereShopVisit;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CereShopVisitServiceImpl implements CereShopVisitService {

    @Autowired
    private CereShopVisitDAO cereShopVisitDAO;

    @Override
    public void insert(CereShopVisit visit) throws CoBusinessException {
        cereShopVisitDAO.insert(visit);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereShopVisitDAO.updateBuyerData(buyerUserId,id);
    }
}
