/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop.impl;


import com.shop.cereshop.app.dao.shop.CereShopReturnDAO;
import com.shop.cereshop.app.service.log.CerePlatformLogService;
import com.shop.cereshop.app.service.shop.CereShopReturnService;
import com.shop.cereshop.commons.domain.shop.CereShopReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CereShopReturnServiceImpl implements CereShopReturnService {

    @Autowired
    private CereShopReturnDAO cereShopReturnDAO;

    @Autowired
    private CerePlatformLogService cerePlatformLogService;

    @Override
    public CereShopReturn findByShopId(Long shopId) {
        return cereShopReturnDAO.findByShopId(shopId);
    }
}
