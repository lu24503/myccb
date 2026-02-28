/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.logistics.impl;

import com.shop.cereshop.app.dao.logistics.CereOrderLogisticsDAO;
import com.shop.cereshop.app.service.logistics.CereOrderLogisticsService;
import com.shop.cereshop.commons.domain.logistics.CereLogisticsCharge;
import com.shop.cereshop.commons.domain.logistics.CereOrderLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereOrderLogisticsServiceImpl implements CereOrderLogisticsService {

    @Autowired
    private CereOrderLogisticsDAO cereOrderLogisticsDAO;

    @Override
    public List<CereOrderLogistics> findLogistics(Long shopId) {
        return cereOrderLogisticsDAO.findLogistics(shopId);
    }

    @Override
    public List<CereLogisticsCharge> findCharges(Long logisticsId) {
        return cereOrderLogisticsDAO.findCharges(logisticsId);
    }
}
