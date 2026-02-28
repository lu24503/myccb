/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop.impl;

import com.shop.cereshop.app.dao.shop.CereShopConversionDAO;
import com.shop.cereshop.app.service.shop.CereShopConversionService;
import com.shop.cereshop.commons.domain.shop.CereShopConversion;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereShopConversionServiceImpl implements CereShopConversionService {

    @Autowired
    private CereShopConversionDAO cereShopConversionDAO;

    @Override
    public void insert(CereShopConversion cereShopConversion) throws CoBusinessException {
        cereShopConversionDAO.insert(cereShopConversion);
    }

    @Override
    public void insertBatch(List<CereShopConversion> conversions) throws CoBusinessException {
        cereShopConversionDAO.insertBatch(conversions);
    }
}
