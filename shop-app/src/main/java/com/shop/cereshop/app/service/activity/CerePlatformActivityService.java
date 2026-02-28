/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.activity;

import com.shop.cereshop.app.page.coupon.CreditCoupon;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.param.canvas.CanvasCouponParam;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.commons.domain.activity.CerePlatformActivity;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CerePlatformActivityService {
    public Page getCoupons(CanvasCouponParam param, CereBuyerUser user) throws CoBusinessException;

    List<ProductCoupon> findCouponByProductId(Long productId);

    List<ProductCoupon> findCouponByProductIdAndUserId(Long buyerUserId, Long productId);

    CerePlatformActivity findById(Long couponId);

    void updateByPrimaryKeySelective(CerePlatformActivity cerePlatformActivity) throws CoBusinessException;

    List<CerePlatformActivity> findOnGoingCouponByBatchId(List<Long> couponIdList);

    List<ToolProduct> getGroupWorkProducts(RenovationParam param);

    Page<CreditCoupon> selectCreditCouponList(Long buyerUserId, PageParam param);

    List<Long> findProductIdList(Long couponId);
}
