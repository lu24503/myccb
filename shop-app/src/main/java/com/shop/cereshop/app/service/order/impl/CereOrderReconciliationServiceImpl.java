/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.order.impl;

import com.shop.cereshop.app.dao.order.CereOrderReconciliationDAO;
import com.shop.cereshop.app.service.order.CereOrderReconciliationService;
import com.shop.cereshop.commons.domain.order.CereOrderReconciliation;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CereOrderReconciliationServiceImpl implements CereOrderReconciliationService {

    @Autowired
    private CereOrderReconciliationDAO cereOrderReconciliationDAO;

    @Override
    public void insert(CereOrderReconciliation reconciliation) throws Exception {
        cereOrderReconciliationDAO.insert(reconciliation);
    }

    @Override
    public void updateByOrderId(CereOrderReconciliation reconciliation) throws CoBusinessException {
        cereOrderReconciliationDAO.updateByOrderId(reconciliation);
    }
}
