/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.price;

import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.price.ShopPriceDetail;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.app.param.price.CereShopPriceDTO;
import com.shop.cereshop.app.param.price.ShopPricePageParam;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.tool.CerePriceRule;

import java.util.List;
import java.util.Map;

public interface CereShopPriceService {

    Map<Long, Map<Long, List<CerePriceRule>>> selectPriceMap(List<Long> shopIdList);

    Page<Product> selectProductListByPriceId(ShopPricePageParam param);

    List<ShopPriceDetail> getPrices(RenovationParam param);

    List<CereShopPriceDTO> selectByShopIdList(List<Long> shopIdList);

    Long selectPriceByProductId(Long productId);
}
