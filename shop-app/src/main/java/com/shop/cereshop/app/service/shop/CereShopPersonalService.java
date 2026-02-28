/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop;

import com.shop.cereshop.app.param.shop.CereShopPersonalParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.shop.CereShopPersonal;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CereShopPersonalService {
    void personal(CereShopPersonal personal, CereBuyerUser user) throws CoBusinessException;

    void updatePersonal(CereShopPersonal personal, CereBuyerUser user) throws CoBusinessException;

    CereShopPersonal findByShopId(Long shopId);

    void personalCheck(CereShopPersonalParam param, CereBuyerUser user) throws CoBusinessException;

    void updatePersonalCheck(CereShopPersonalParam param, CereBuyerUser user) throws CoBusinessException;
}
