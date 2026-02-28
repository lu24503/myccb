/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.permission.impl;

import com.shop.cereshop.app.dao.permission.CerePlatformPermissionDAO;
import com.shop.cereshop.app.service.permission.CerePlatformPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CerePlatformPermissionServiceImpl implements CerePlatformPermissionService {

    @Autowired
    private CerePlatformPermissionDAO cerePlatformPermissionDAO;

}
