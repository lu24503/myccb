/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.order;

import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.commons.domain.order.CereOrderProduct;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereOrderProductService {
    void insert(CereOrderProduct orderProduct) throws CoBusinessException;

    List<CartSku> findOrderProductSku(Long orderId);

    List<CereOrderProduct> findByOrderIds(List<Long> orderIdList);

    List<CartSku> findProductStatsByOrderIds(List<Long> orderIdList);
}
