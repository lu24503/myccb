/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.aspect.risk.handler;

import com.shop.cereshop.app.aspect.risk.AbstractRiskCheckHandler;
import com.shop.cereshop.app.aspect.risk.RiskEvaluateRegistry;
import com.shop.cereshop.app.service.risk.CereRiskBlackService;
import com.shop.cereshop.app.service.risk.CereRiskRuleService;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.risk.CereRiskBlack;
import com.shop.cereshop.commons.domain.risk.CereRiskRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author weicb
 * @date 2021/9/22 00:10
 */
@Component
public class DefaultRiskCheckHandler extends AbstractRiskCheckHandler {

    @Autowired
    private RiskEvaluateRegistry registry;

    @Autowired
    private CereRiskBlackService riskBlackService;

    @Autowired
    private CereRiskRuleService riskRuleService;


    @Override
    protected Long getBuyerUserId() {
        CereBuyerUser user = (CereBuyerUser) super.innerRequest.getAttribute("user");
        if (user != null) {
            return user.getBuyerUserId();
        }
        return null;
    }

    @Override
    protected Optional<CereRiskRule> getRiskRule() {
        return riskRuleService.getEnabled();
    }

    @Override
    protected List<CereRiskBlack> getRiskBlack() {
        return riskBlackService.getEnabled();
    }

    @Override
    protected RiskEvaluateRegistry getRiskEvaluateRegistry() {
        return registry;
    }
}
