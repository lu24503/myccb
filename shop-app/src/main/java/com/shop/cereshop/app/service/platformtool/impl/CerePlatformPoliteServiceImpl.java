/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.platformtool.impl;

import com.shop.cereshop.app.dao.platformtool.CerePlatformPoliteDAO;
import com.shop.cereshop.app.service.platformtool.CerePlatformPoliteService;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformPolite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CerePlatformPoliteServiceImpl implements CerePlatformPoliteService {

    @Autowired
    private CerePlatformPoliteDAO cerePlatformPoliteDAO;

    @Override
    public CerePlatformPolite selectOnGoingPolite() {
        return cerePlatformPoliteDAO.selectOnGoingPolite();
    }
}
