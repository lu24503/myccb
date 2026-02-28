/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.aspect.risk.evaluate;

import com.alibaba.fastjson.JSONObject;
import com.shop.cereshop.app.aspect.risk.rule.Evaluate;
import com.shop.cereshop.app.aspect.risk.rule.RiskFactor;
import com.shop.cereshop.app.service.after.CereOrderAfterService;
import com.shop.cereshop.commons.domain.risk.CereRiskRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 按照售后进行风控
 * @author weicb
 * @date 2021/10/21 00:34
 */
@Component
public class PostSalesEvaluate extends Evaluate {

    @Autowired
    private CereOrderAfterService cereOrderAfterService;

    public PostSalesEvaluate() {
        super(RiskFactor.POST_NUM);
    }

    @Override
    public boolean needCheck(CereRiskRule rule) {
        return rule.getRuleSwitchPostSale() == 1;
    }

    @Override
    public boolean eval(CereRiskRule rule, Long buyerUserId, Object[] args) {
        JSONObject jsonObject = JSONObject.parseObject(rule.getRulePostSaleLimit());
        if (!jsonObject.containsKey("timeNum") || !jsonObject.containsKey("num") || !jsonObject.containsKey("compare")) {
            return true;
        }
        Integer timeNum = jsonObject.getObject("timeNum", Integer.class);
        Integer num = jsonObject.getObject("num", Integer.class);
        String compare = jsonObject.getObject("compare", String.class);

        Integer total = cereOrderAfterService.getPostSaleNumByMonth(timeNum, buyerUserId);

        return !calc(compare, total, num);
    }
}
