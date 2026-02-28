/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer.impl;

import com.shop.cereshop.app.dao.buyer.CereBuyerSearchDAO;
import com.shop.cereshop.app.service.buyer.CereBuyerSearchService;
import com.shop.cereshop.commons.domain.buyer.CereBuyerSearch;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereBuyerSearchServiceImpl implements CereBuyerSearchService {

    @Autowired
    private CereBuyerSearchDAO cereBuyerSearchDAO;

    @Override
    public List<CereBuyerSearch> getHistory(Long buyerUserId) {
        return cereBuyerSearchDAO.getHistory(buyerUserId);
    }

    @Override
    public void insert(CereBuyerSearch cereBuyerSearch) throws CoBusinessException {
        cereBuyerSearchDAO.insert(cereBuyerSearch);
    }

    @Override
    public void delete(Long searchId) {
        cereBuyerSearchDAO.deleteByPrimaryKey(searchId);
    }

    @Override
    public CereBuyerSearch findBySearch(String search, Long buyerUserId) {
        return cereBuyerSearchDAO.findBySearch(search,buyerUserId);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereBuyerSearchDAO.updateBuyerData(buyerUserId,id);
    }

    @Override
    public List<String> selectHotSearch() {
        return cereBuyerSearchDAO.selectHotSearch();
    }
}
