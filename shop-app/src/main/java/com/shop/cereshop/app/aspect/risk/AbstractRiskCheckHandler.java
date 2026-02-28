/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.aspect.risk;

import cn.hutool.core.collection.CollectionUtil;
import com.shop.cereshop.app.aspect.risk.rule.Evaluate;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.risk.CereRiskBlack;
import com.shop.cereshop.commons.domain.risk.CereRiskRule;
import com.shop.cereshop.commons.utils.AppletPayUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author weicb
 * @date 2021/9/21 17:15
 */
public abstract class AbstractRiskCheckHandler implements RiskCheckHandler {

    protected HttpServletRequest innerRequest;

    /**
     * 风控拦截判断，用户/ip是黑名单或者命中配置的风控规则，则需要拦截
     * 返回 true 代表不触发风控，返回 false 表示触发风控规则，禁止下单
     */
    @Override
    public boolean check(HttpServletRequest request, Object[] args) {
        innerRequest = request;
        List<CereRiskBlack> riskBlacks = getRiskBlack();
        // 黑名单：ip 黑名单，不允许下单
        boolean blackIp = blackIp(riskBlacks, AppletPayUtil.getClientIp(request));
        if (blackIp) { return false;  }

        // 黑名单：用户黑名单，不允许下单
        boolean blackUser = blackUser(riskBlacks);
        if (blackUser) { return false;  }

        return riskRuleCheck(args);
    }

    /**
     * 其他风控规则，不允许下单返回 false
     */
    private boolean riskRuleCheck(Object[] args) {
        Optional<CereRiskRule> riskRuleOpt = getRiskRule();
        // 无需风控拦截
        if (!riskRuleOpt.isPresent()) { return true; }
        CereRiskRule rule = riskRuleOpt.get();

        // 过滤无需检查的规则
        List<Evaluate> evaluates = getRiskEvaluateRegistry().getEvaluates().stream()
                .filter(e -> e.needCheck(rule)).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(evaluates)) {
            return true;
        }

        boolean anyMatch = IntegerEnum.RISK_RULE_TYPE_ANY.getCode().equals(rule.getRuleType());
        boolean result = false;
        for(Evaluate eval : evaluates) {
            boolean passed = eval.eval(rule, getBuyerUserId(), args);
            // 满足任一规则，只要有不通过的，即触发风控，不允许下单
            if (anyMatch && !passed) {
                return false;
            }
            // 否则，只要有通过的，就通过
            if (!anyMatch && passed) {
                return true;
            }
            result = passed;
        }
        return result;
    }

    /**
     * 判断用户是否允许下单，为黑名单则不允许，不允许返回 true
     */
    protected boolean blackUser(List<CereRiskBlack> riskBlacks) {
        Long buyerUserId = getBuyerUserId();
        return riskBlacks.stream().filter(r -> IntegerEnum.RISK_BLACK_TYPE_USER.getCode().equals(r.getType()))
                .anyMatch(r -> buyerUserId.equals(r.getBuyerUserId()));
    }

    /**
     * 判断ip是否允许下单，为黑名单则不允许，不允许返回 true
     */
    private boolean blackIp(List<CereRiskBlack> riskBlacks, String ip) {
        return riskBlacks.stream().filter(r -> IntegerEnum.RISK_BLACK_TYPE_IP.getCode().equals(r.getType()))
                .anyMatch(r -> Arrays.asList(r.getIp().split(",")).contains(ip));
    }


    /**
     * 获取当前登录用户
     */
    protected abstract Long getBuyerUserId();

    /**
     * 风控规则
     */
    protected abstract Optional<CereRiskRule> getRiskRule();

    /**
     * 风控黑名单
     */
    protected abstract List<CereRiskBlack> getRiskBlack();

    /**
     * 规则注册器
     */
    protected abstract RiskEvaluateRegistry getRiskEvaluateRegistry();

}
