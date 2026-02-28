/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.sensitive.impl;

import com.shop.cereshop.app.dao.sensitive.CerePlatformSensitiveDAO;
import com.shop.cereshop.app.service.sensitive.CerePlatformSensitiveService;
import com.shop.cereshop.commons.domain.sensitive.CerePlatformSensitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CerePlatformSensitiveServiceImpl implements CerePlatformSensitiveService {

    @Autowired
    private CerePlatformSensitiveDAO cerePlatformSensitiveDAO;

    @Override
    public CerePlatformSensitive findSensitive() {
        return cerePlatformSensitiveDAO.findSensitive();
    }
}
