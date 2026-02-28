/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop.impl;

import com.shop.cereshop.app.dao.shop.CereShopExtensionDAO;
import com.shop.cereshop.app.page.shop.Extension;
import com.shop.cereshop.app.service.shop.CereShopExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CereShopExtensionServiceImpl implements CereShopExtensionService {

    @Autowired
    private CereShopExtensionDAO cereShopExtensionDAO;

    @Override
    public Extension findByShopIdAndTitle(Long shopId, String title) {
        return cereShopExtensionDAO.findByShopIdAndTitle(shopId,title);
    }
}
