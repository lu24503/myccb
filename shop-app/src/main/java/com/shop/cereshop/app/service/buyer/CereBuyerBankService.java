/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer;

import com.shop.cereshop.app.param.buyer.BankParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerBank;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereBuyerBankService {
    void save(CereBuyerBank bank, CereBuyerUser user) throws CoBusinessException;

    void update(CereBuyerBank bank, CereBuyerUser user) throws CoBusinessException;

    void delete(Long bankId,CereBuyerUser user) throws CoBusinessException;

    CereBuyerBank getById(Long bankId) throws CoBusinessException;

    Page getAll(BankParam param) throws CoBusinessException;

    List<CereBuyerBank> getSelect(Long buyerUserId) throws CoBusinessException;

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;
}
