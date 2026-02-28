/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop;

import com.shop.cereshop.app.param.shop.CereShopOtherOrganizationsParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.shop.CereShopOtherOrganizations;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CereShopOtherOrganizationsService {
    void organizations(CereShopOtherOrganizations otherOrganizations, CereBuyerUser user) throws CoBusinessException;

    void updateOrganizations(CereShopOtherOrganizations otherOrganizations, CereBuyerUser user) throws CoBusinessException;

    CereShopOtherOrganizations findByShopId(Long shopId);

    void organizationsCheck(CereShopOtherOrganizationsParam param, CereBuyerUser user) throws CoBusinessException;

    void updateOrganizationsCheck(CereShopOtherOrganizationsParam param, CereBuyerUser user) throws CoBusinessException;
}
