/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop;

import com.shop.cereshop.app.param.shop.CereShopEnterpriseParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.shop.CereShopEnterprise;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CereShopEnterpriseService {
    void enterprise(CereShopEnterprise enterprise, CereBuyerUser user) throws CoBusinessException;

    void updateEnterprise(CereShopEnterprise enterprise, CereBuyerUser user) throws CoBusinessException;

    CereShopEnterprise findByShopId(Long shopId);

    void enterpriseCheck(CereShopEnterpriseParam param, CereBuyerUser user) throws CoBusinessException;

    void updateEnterpriseCheck(CereShopEnterpriseParam param, CereBuyerUser user) throws CoBusinessException;
}
