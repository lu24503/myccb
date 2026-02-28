/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.risk;


import com.shop.cereshop.commons.domain.risk.CereRiskRule;

import java.util.Optional;

/**
 * <p>
 * 业务接口
 * 风控规则表
 * </p>
 *
 * @author JustArgo
 * @date 2021-12-06
 */
public interface CereRiskRuleService {

    Optional<CereRiskRule> getEnabled();
}
