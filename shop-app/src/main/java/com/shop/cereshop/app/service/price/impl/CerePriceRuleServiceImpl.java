/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.price.impl;

import com.shop.cereshop.app.dao.price.CerePriceRuleDAO;
import com.shop.cereshop.app.service.price.CerePriceRuleService;
import com.shop.cereshop.commons.domain.tool.CerePriceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CerePriceRuleServiceImpl implements CerePriceRuleService {

    @Autowired
    private CerePriceRuleDAO cerePriceRuleDAO;

    @Override
    public List<CerePriceRule> findRules(Long priceId) {
        return cerePriceRuleDAO.findRules(priceId);
    }

    @Override
    public List<CerePriceRule> findRulesByShopId(Long shopId) {
        return cerePriceRuleDAO.findRulesByShopId(shopId);
    }
}
