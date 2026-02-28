/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.logistics;

import com.shop.cereshop.commons.domain.logistics.CereLogisticsCharge;
import com.shop.cereshop.commons.domain.logistics.CereOrderLogistics;

import java.util.List;

public interface CereOrderLogisticsService {
    List<CereOrderLogistics> findLogistics(Long shopId);

    List<CereLogisticsCharge> findCharges(Long logisticsId);
}
