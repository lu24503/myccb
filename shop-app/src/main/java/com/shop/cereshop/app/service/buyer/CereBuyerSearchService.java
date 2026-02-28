/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer;

import com.shop.cereshop.commons.domain.buyer.CereBuyerSearch;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereBuyerSearchService {
    List<CereBuyerSearch> getHistory(Long buyerUserId);

    void insert(CereBuyerSearch cereBuyerSearch) throws CoBusinessException;

    void delete(Long searchId);

    CereBuyerSearch findBySearch(String search, Long buyerUserId);

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;

    List<String> selectHotSearch();
}
