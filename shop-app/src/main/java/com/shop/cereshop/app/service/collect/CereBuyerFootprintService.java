/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.collect;

import com.shop.cereshop.app.param.collect.FootprintIdParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.collect.CereBuyerFootprint;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CereBuyerFootprintService {
    void insert(CereBuyerFootprint cereBuyerFootprint) throws CoBusinessException;

    void delete(FootprintIdParam param, CereBuyerUser user) throws CoBusinessException;

    Page getAll(PageParam param, Long buyerUserId) throws CoBusinessException;

    void selected(FootprintIdParam param, CereBuyerUser user) throws CoBusinessException;

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;

    int findFootprintCount(Long buyerUserId);
}
