/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.order;

import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.order.OrderProductAttribute;
import com.shop.cereshop.commons.domain.order.CereOrderProductAttribute;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereOrderProductAttributeService {
    List<CereOrderProductAttribute> findBySkuId(Long skuId) ;

    void insertBatch(List<OrderProductAttribute> attributes) throws CoBusinessException;

    List<OrderProductAttribute> findBySkus(List<CartSku> skus);
}
