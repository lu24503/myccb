/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.after.impl;

import com.shop.cereshop.app.dao.after.CerePlatformAfterDAO;
import com.shop.cereshop.app.service.after.CerePlatformAfterService;
import com.shop.cereshop.commons.domain.after.CerePlatformAfter;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CerePlatformAfterServiceImpl implements CerePlatformAfterService {

    @Autowired
    private CerePlatformAfterDAO cerePlatformAfterDAO;

    @Override
    public void insert(CerePlatformAfter cerePlatformAfter) throws CoBusinessException {
        cerePlatformAfterDAO.insert(cerePlatformAfter);
    }
}
