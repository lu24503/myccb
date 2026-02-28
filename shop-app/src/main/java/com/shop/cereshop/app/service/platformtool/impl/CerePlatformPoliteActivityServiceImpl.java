/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.platformtool.impl;

import com.shop.cereshop.app.dao.platformtool.CerePlatformPoliteActivityDAO;
import com.shop.cereshop.app.service.platformtool.CerePlatformPoliteActivityService;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformPoliteActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CerePlatformPoliteActivityServiceImpl implements CerePlatformPoliteActivityService {

    @Autowired
    private CerePlatformPoliteActivityDAO cerePlatformPoliteActivityDAO;

    @Override
    public List<CerePlatformPoliteActivity> selectByPoliteId(Long politeId) {
        return cerePlatformPoliteActivityDAO.selectByPoliteId(politeId);
    }
}
