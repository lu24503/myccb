/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.activity.impl;

import com.shop.cereshop.app.dao.activity.CereSignProductDAO;
import com.shop.cereshop.app.service.activity.CereSignProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CereSignProductServiceImpl implements CereSignProductService {

    @Autowired
    private CereSignProductDAO cereSignProductDAO;

}
