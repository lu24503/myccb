/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop;

import com.shop.cereshop.commons.domain.shop.CereShopVisit;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CereShopVisitService {
    void insert(CereShopVisit visit) throws CoBusinessException;

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;
}
