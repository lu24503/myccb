/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.after;

import com.shop.cereshop.commons.domain.after.CereAfterProductAttribute;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereAfterProductAttributeService {

    void insertBatch(List<CereAfterProductAttribute> attributes) throws CoBusinessException;

}
