/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.log.impl;

import com.shop.cereshop.app.dao.log.CerePlatformWebLogDAO;
import com.shop.cereshop.app.service.log.CerePlatformWebLogService;
import com.shop.cereshop.commons.domain.log.CerePlatformWebLog;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CerePlatformWebLogServiceImpl implements CerePlatformWebLogService {

    @Autowired
    private CerePlatformWebLogDAO cerePlatformWebLogDAO;

    @Override
    public void insert(CerePlatformWebLog webLog) throws CoBusinessException {
        cerePlatformWebLogDAO.insert(webLog);
    }
}
