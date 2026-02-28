/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.discount;

import com.shop.cereshop.app.page.discount.DiscountIndex;
import com.shop.cereshop.app.page.discount.ShopDiscountDetail;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.param.discount.DiscountParam;
import com.shop.cereshop.app.param.product.ProductParam;
import com.shop.cereshop.app.param.renovation.RenovationParam;
import com.shop.cereshop.app.param.seckill.ShopBusinessDiscount;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.tool.CereShopDiscount;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereShopDiscountService {
    DiscountIndex getIndex(DiscountParam param, CereBuyerUser user) throws CoBusinessException,Exception;

    @Deprecated
    ProductDetail getById(CereBuyerUser user, ProductParam param, ProductDetail detail) throws CoBusinessException,Exception;

    CereShopDiscount findById(Long shopDiscountId);

    Long findByProductId(Long skuId);

    List<ShopDiscountDetail> getDiscounts(RenovationParam param);

    List<ShopBusinessDiscount> selectByShopIdList(List<Long> shopIdList);

    ProductDetail setActivityInfo(Long shopDiscountId, CereBuyerUser user, ProductParam param, ProductDetail detail);

    ProductDetail setActivityInfoForRealInfo(Long shopDiscountId, CereBuyerUser user, ProductParam param, ProductDetail detail);
}
