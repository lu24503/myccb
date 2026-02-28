/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.distributor.impl;

import com.shop.cereshop.app.dao.distributor.CereDistributorBuyerDAO;
import com.shop.cereshop.app.page.order.ShopDistributor;
import com.shop.cereshop.app.service.distributor.CereDistributorBuyerService;
import com.shop.cereshop.commons.domain.distributor.CereDistributorBuyer;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CereDistributorBuyerServiceImpl implements CereDistributorBuyerService {

    @Autowired
    private CereDistributorBuyerDAO cereDistributorBuyerDAO;

    @Override
    public ShopDistributor findByUserId(Long buyerUserId, Long shopId) {
        return cereDistributorBuyerDAO.findByUserId(buyerUserId, shopId);
    }

    @Override
    public CereDistributorBuyer findByDisAndUser(Long distributorId, Long buyerUserId) {
        return cereDistributorBuyerDAO.findByDisAndUser(distributorId,buyerUserId);
    }

    @Override
    public CereDistributorBuyer checkUser(Long distributorId, Long buyerUserId) {
        return cereDistributorBuyerDAO.checkUser(distributorId,buyerUserId);
    }

    @Override
    public void update(CereDistributorBuyer distributorBuyer) throws CoBusinessException {
        cereDistributorBuyerDAO.updateIfBind(distributorBuyer);
    }

    @Override
    public void insert(CereDistributorBuyer cereDistributorBuyer) throws CoBusinessException {
        cereDistributorBuyerDAO.insert(cereDistributorBuyer);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereDistributorBuyerDAO.updateBuyerData(buyerUserId,id);
    }
}
