/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer.impl;

import com.shop.cereshop.app.dao.buyer.CereBuyerLabelDAO;
import com.shop.cereshop.app.service.buyer.CereBuyerLabelService;
import com.shop.cereshop.commons.domain.label.CereBuyerLabel;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CereBuyerLabelServiceImpl implements CereBuyerLabelService {

    @Autowired
    private CereBuyerLabelDAO cereBuyerLabelDAO;

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereBuyerLabelDAO.updateBuyerData(buyerUserId,id);
    }
}
