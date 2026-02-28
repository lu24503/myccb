/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.business;

import com.shop.cereshop.commons.domain.business.CerePlatformBusiness;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CerePlatformBusinessService {
    void insert(CerePlatformBusiness cerePlatformBusiness) throws CoBusinessException;

    CerePlatformBusiness checkUserName(String shopPhone);

    CerePlatformBusiness checkPhone(String phone);
}
