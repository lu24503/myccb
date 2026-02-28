/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.business.impl;

import com.shop.cereshop.app.dao.business.CereBusinessBuyerUserDAO;
import com.shop.cereshop.app.service.business.CereBusinessBuyerUserService;
import com.shop.cereshop.commons.domain.business.CereBusinessBuyerUser;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereBusinessBuyerUserServiceImpl implements CereBusinessBuyerUserService {

    @Autowired
    private CereBusinessBuyerUserDAO cereBusinessBuyerUserDAO;

    @Override
    public void addBusinessBuyerUser(CereBusinessBuyerUser cereBusinessBuyerUser) {
        cereBusinessBuyerUser.setCreateTime(TimeUtils.yyMMddHHmmss());
        cereBusinessBuyerUserDAO.insertOrUpdate(cereBusinessBuyerUser);
    }

    @Override
    public void refreshUpdateTime(CereBusinessBuyerUser bbu) {
        cereBusinessBuyerUserDAO.refreshUpdateTime(bbu);
    }

    @Override
    public List<CereBuyerUser> selectBuyerUserByShopId(Long shopId) {
        return cereBusinessBuyerUserDAO.selectBuyerUserByShopId(shopId);
    }
}
