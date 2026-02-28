/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.distributor;

import com.shop.cereshop.app.page.distributor.LevelDescDTO;

import java.util.List;

public interface CereShopDistributionLevelService {

    List<LevelDescDTO> getDistributionLevelConfig(Long shopId);
}
