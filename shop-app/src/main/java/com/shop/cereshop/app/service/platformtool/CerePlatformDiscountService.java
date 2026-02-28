/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.platformtool;

import com.shop.cereshop.app.page.canvas.CanvasPlatformDiscount;
import com.shop.cereshop.app.page.discount.PlatformDiscountProduct;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.app.param.discount.PlatformDiscountParam;
import com.shop.cereshop.app.param.discount.ShopPlatformDiscount;
import com.shop.cereshop.commons.domain.buyer.CereBuyerPlatformDiscountVisit;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformDiscount;

import java.util.List;

public interface CerePlatformDiscountService {

    CerePlatformDiscount queryPlatformDiscount(Long discountId);

    Page<PlatformDiscountProduct> queryPlatformDiscountProductList(PlatformDiscountParam param);

    List<CanvasPlatformDiscount> getMinDiscount(RenovationParam param);

    CerePlatformDiscount findById(Long platformDiscountId);

    void insertVisit(CereBuyerPlatformDiscountVisit cereBuyerPlatformDiscountVisit);

    List<ShopPlatformDiscount> selectPlatformDiscountByShopIdList(List<Long> shopIdList);

    ProductDetail setActivityInfo(Long platformDiscountId, CereBuyerUser user, ProductDetail detail);

    ProductDetail setActivityInfoForRealInfo(Long platformDiscountId, CereBuyerUser user, ProductDetail detail);
}
