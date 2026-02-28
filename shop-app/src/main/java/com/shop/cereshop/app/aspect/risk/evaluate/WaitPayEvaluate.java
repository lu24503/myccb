/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.aspect.risk.evaluate;

import com.alibaba.fastjson.JSONObject;
import com.shop.cereshop.app.aspect.risk.rule.Evaluate;
import com.shop.cereshop.app.service.order.CereShopOrderService;
import com.shop.cereshop.commons.domain.risk.CereRiskRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 按照待支付进行风控
 * @author weicb
 * @date 2021/10/21 00:34
 */
@Component
public class WaitPayEvaluate extends Evaluate {

    @Autowired
    private CereShopOrderService cereShopOrderService;

    public WaitPayEvaluate() {
        super(com.shop.cereshop.app.aspect.risk.rule.RiskFactor.WAIT_PAY_NUM);
    }

    @Override
    public boolean needCheck(CereRiskRule rule) {
        return rule.getRuleSwitchWaitPay() == 1;
    }

    @Override
    public boolean eval(CereRiskRule rule, Long buyerUserId, Object[] args) {
        JSONObject jsonObject = JSONObject.parseObject(rule.getRuleWaitPayLimit());
        if (!jsonObject.containsKey("timeNum") || !jsonObject.containsKey("num") || !jsonObject.containsKey("compare")) {
            return true;
        }
        Integer timeNum = jsonObject.getObject("timeNum", Integer.class);
        Integer num = jsonObject.getObject("num", Integer.class);
        String compare = jsonObject.getObject("compare", String.class);

        Integer total = cereShopOrderService.getWaitPayNumByHours(timeNum, buyerUserId);

        return !calc(compare, total, num);
    }
}
