/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.business;

import com.shop.cereshop.commons.domain.business.CereBusinessBuyerUser;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;

import java.util.List;

public interface CereBusinessBuyerUserService {
    void addBusinessBuyerUser(CereBusinessBuyerUser cereBusinessBuyerUser);

    void refreshUpdateTime(CereBusinessBuyerUser bbu);

    List<CereBuyerUser> selectBuyerUserByShopId(Long shopId);
}
