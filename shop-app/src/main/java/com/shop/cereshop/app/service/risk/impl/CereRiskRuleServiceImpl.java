/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.risk.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.cereshop.app.dao.risk.CereRiskRuleDAO;
import com.shop.cereshop.app.service.risk.CereRiskRuleService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.risk.CereRiskRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 业务实现类
 * 风控规则表
 * </p>
 *
 * @author JustArgo
 * @date 2021-12-06
 */
@Slf4j
@Service
public class CereRiskRuleServiceImpl implements CereRiskRuleService {

    @Autowired
    private CereRiskRuleDAO cereRiskRuleDAO;

    @Override
    public Optional<CereRiskRule> getEnabled() {
        LambdaQueryWrapper<CereRiskRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CereRiskRule::getStatus, IntegerEnum.YES.getCode());
        wrapper.orderByDesc(CereRiskRule::getUpdateTime);
        List<CereRiskRule> ruleList = cereRiskRuleDAO.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(ruleList)) {
            return Optional.of(ruleList.get(0));
        }
        //不能改为.empty()
        return Optional.ofNullable(null);
    }
}
