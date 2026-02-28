/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.role.impl;

import com.shop.cereshop.app.dao.role.CerePlatformRolePermissionDAO;
import com.shop.cereshop.app.service.role.CerePlatformRolePermissionService;
import com.shop.cereshop.commons.domain.role.CerePlatformRolePermission;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CerePlatformRolePermissionServiceImpl implements CerePlatformRolePermissionService {

    @Autowired
    private CerePlatformRolePermissionDAO cerePlatformRolePermissionDAO;

}
