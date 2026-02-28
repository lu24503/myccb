/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop;

import com.shop.cereshop.commons.domain.shop.CereShopConversion;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereShopConversionService {
    void insert(CereShopConversion cereShopConversion) throws CoBusinessException;

    void insertBatch(List<CereShopConversion> conversions) throws CoBusinessException;
}
