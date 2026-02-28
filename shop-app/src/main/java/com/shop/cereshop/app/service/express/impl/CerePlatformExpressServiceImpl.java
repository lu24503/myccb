/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.express.impl;

import com.shop.cereshop.app.dao.express.CerePlatformExpressDAO;
import com.shop.cereshop.app.service.express.CerePlatformExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CerePlatformExpressServiceImpl implements CerePlatformExpressService {

    @Autowired
    private CerePlatformExpressDAO cerePlatformExpressDAO;

    @Override
    public Integer findExpress() {
        return cerePlatformExpressDAO.findExpress();
    }
}
