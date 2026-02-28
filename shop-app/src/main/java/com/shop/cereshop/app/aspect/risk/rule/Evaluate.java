/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.aspect.risk.rule;


import com.shop.cereshop.commons.domain.risk.CereRiskRule;

/**
 * @author weicb
 * @date 2021/9/21 19:05
 */
public abstract class Evaluate {

    private RiskFactor RiskFactor;

    public static final String GT = "gt";
    public static final String GE = "ge";
    public static final String LT = "lt";
    public static final String LE = "le";
    public static final String EQ = "eq";


    public RiskFactor getRiskFactor() {
        return RiskFactor;
    }

    public Evaluate(RiskFactor riskFactor) {
        RiskFactor = riskFactor;
    }

    public abstract boolean needCheck(CereRiskRule rule);

    protected boolean calc(String compare, Integer leftValue, Integer rightValue) {
        switch (compare) {
            case GT:
                return leftValue > rightValue;
            case GE:
                return leftValue >= rightValue;
            case LT:
                return leftValue < rightValue;
            case LE:
                return leftValue <= rightValue;
            case EQ:
                return leftValue.equals(rightValue);
            default:
                break;
        }

        return true;
    }

    /**
     * 计算是否触发风控规则
     */
    public abstract boolean eval(CereRiskRule rule, Long buyerUserId, Object[] args);


}
