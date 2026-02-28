/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer;

import com.shop.cereshop.commons.domain.buyer.CereBuyerReceive;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereBuyerReceiveService {
    CereBuyerReceive findlatelyReceiveByUserId(Long buyerUserId);

    CereBuyerReceive save(CereBuyerReceive receive, CereBuyerUser user) throws CoBusinessException;

    void update(CereBuyerReceive receive, CereBuyerUser user) throws CoBusinessException;

    void delete(Long receiveId, CereBuyerUser user) throws CoBusinessException;

    CereBuyerReceive getById(Long receiveId) throws CoBusinessException;

    Page getAll(PageParam param, CereBuyerUser user) throws CoBusinessException;

    List<CereBuyerReceive> getSelect(Long buyerUserId) throws CoBusinessException;

    CereBuyerReceive findById(Long receiveId);

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;
}
