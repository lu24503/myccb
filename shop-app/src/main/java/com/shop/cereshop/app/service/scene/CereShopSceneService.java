/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.scene;

import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.scene.CereShopScene;

import java.util.List;

public interface CereShopSceneService {

    List<CereShopScene> selectOnGoingFestivalMarketing();

    List<CereShopScene> selectOnGoingHolidayMarketing();

    List<CereShopScene> selectOnGoingBirthDayMarketing();

    List<CereShopScene> selectOnGoingMarketingByShopId(Long shopId);

    ProductDetail setActivityInfo(CereBuyerUser user, ProductDetail detail);

    void batchSetActivityInfo(CereBuyerUser user, List<Product> detailList);

    List<CereShopScene> selectOnGoingMarketing();

    ProductDetail setActivityInfoForRealInfo(CereBuyerUser user, ProductDetail detail);
}
