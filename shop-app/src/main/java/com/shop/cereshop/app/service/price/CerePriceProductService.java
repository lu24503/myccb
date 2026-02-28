/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.price;

import com.shop.cereshop.app.page.compose.ComposeProduct;
import com.shop.cereshop.app.param.canvas.CanvasProductParam;
import com.shop.cereshop.app.param.price.ShopPricePageParam;
import com.shop.cereshop.commons.domain.common.Page;

import java.util.List;
import java.util.Map;

public interface CerePriceProductService {

    Map<Long, List<Long>> selectProductIdListMap(List<Long> priceIdList, List<Long> productIdList);

    Page getPriceProducts(CanvasProductParam param);

    Page<ComposeProduct> findProducts(ShopPricePageParam priceParam);
}
