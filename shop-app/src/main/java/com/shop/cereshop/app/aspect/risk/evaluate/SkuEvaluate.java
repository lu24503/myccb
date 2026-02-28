/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.aspect.risk.evaluate;

import com.alibaba.fastjson.JSONObject;
import com.shop.cereshop.app.aspect.risk.rule.Evaluate;
import com.shop.cereshop.app.aspect.risk.rule.RiskFactor;
import com.shop.cereshop.app.param.order.OrderParam;
import com.shop.cereshop.app.param.order.OrderProductParam;
import com.shop.cereshop.app.param.settlement.ProductSku;
import com.shop.cereshop.commons.domain.risk.CereRiskRule;
import org.springframework.stereotype.Component;

/**
 * @author weicb
 * @date 2021/10/21 00:34
 */
@Component
public class SkuEvaluate extends Evaluate {

    public SkuEvaluate() {
        super(RiskFactor.WAIT_PAY_NUM);
    }

    @Override
    public boolean needCheck(CereRiskRule rule) {
        return rule.getRuleSwitchSku() == 1;
    }

    @Override
    public boolean eval(CereRiskRule rule, Long buyerUserId, Object[] args) {
        JSONObject jsonObject = JSONObject.parseObject(rule.getRuleSkuLimit());
        if (!jsonObject.containsKey("num") || !jsonObject.containsKey("compare")) {
            return true;
        }
        Integer num = jsonObject.getObject("num", Integer.class);
        String compare = jsonObject.getObject("compare", String.class);
        if (args != null && args.length > 0) {
            if (args[0] instanceof OrderParam) {
                OrderParam orderParam = (OrderParam)args[0];
                if (orderParam.getShops() != null) {
                    for (OrderProductParam param:orderParam.getShops()) {
                        for (ProductSku sku:param.getSkus()) {
                            //达到风控条件
                            boolean matchCondition = calc(compare, sku.getNumber(), num);
                            if (matchCondition) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;

//        return !calc(compare, total, num);
    }
}
