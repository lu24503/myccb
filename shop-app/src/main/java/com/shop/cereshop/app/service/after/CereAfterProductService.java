/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.after;

import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.param.after.AfterParam;
import com.shop.cereshop.commons.domain.after.CereAfterProduct;
import com.shop.cereshop.commons.domain.after.CereAfterProductAttribute;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereAfterProductService {
    void insert(CereAfterProduct afterProduct) throws CoBusinessException;

    List<CereAfterProductAttribute> findValuesBySkuId(Long skuId);

    List<CartSku> findSkusByAfterId(Long afterId);

    List<CereAfterProduct> findSkuBySkus(AfterParam param);

    void deleteByAfterId(Long id);

    List<CereAfterProduct> findByOrderIdForCheck(Long orderId);
}
