/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer.impl;

import com.shop.cereshop.app.dao.buyer.CereBuyerDiscountVisitDAO;
import com.shop.cereshop.app.dao.buyer.CereBuyerSeckillVisitDAO;
import com.shop.cereshop.app.service.buyer.CereBuyerDiscountVisitService;
import com.shop.cereshop.app.service.buyer.CereBuyerSeckillVisitService;
import com.shop.cereshop.commons.domain.buyer.CereBuyerDiscountVisit;
import com.shop.cereshop.commons.domain.buyer.CereBuyerSeckillVisit;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CereBuyerDiscountVisitServiceImpl implements CereBuyerDiscountVisitService {

    @Autowired
    private CereBuyerDiscountVisitDAO cereBuyerDiscountVisitDAO;

    @Override
    public void insert(CereBuyerDiscountVisit cereBuyerDiscountVisit) throws CoBusinessException {
        cereBuyerDiscountVisitDAO.insert(cereBuyerDiscountVisit);
    }
}
