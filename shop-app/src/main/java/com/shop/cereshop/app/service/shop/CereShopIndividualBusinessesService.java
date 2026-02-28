/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop;

import com.shop.cereshop.app.param.shop.CereShopIndividualBusinessesParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.shop.CereShopIndividualBusinesses;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CereShopIndividualBusinessesService {
    void individual(CereShopIndividualBusinesses individualBusinesses, CereBuyerUser user) throws CoBusinessException;

    void updateIndividual(CereShopIndividualBusinesses individualBusinesses, CereBuyerUser user) throws CoBusinessException;

    CereShopIndividualBusinesses findByShopId(Long shopId);

    void individualCheck(CereShopIndividualBusinessesParam param, CereBuyerUser user) throws CoBusinessException;

    void updateIndividualCheck(CereShopIndividualBusinessesParam param, CereBuyerUser user) throws CoBusinessException;
}
