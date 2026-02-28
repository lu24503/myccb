/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.activity;

import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.param.coupon.ActivityParam;
import com.shop.cereshop.app.param.coupon.CouponListParam;
import com.shop.cereshop.app.param.coupon.CouponParam;
import com.shop.cereshop.app.param.coupon.OrderCouponParam;
import com.shop.cereshop.commons.domain.activity.CereBuyerCoupon;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.math.BigDecimal;
import java.util.List;

public interface CereBuyerCouponService {

    void takeCoupon(CouponParam param, CereBuyerUser user) throws CoBusinessException;

    List<ProductCoupon> getCoupons(Long buyerUserId) throws CoBusinessException;

    Page getCouponProducts(ActivityParam param) throws CoBusinessException;

    List<ProductCoupon> getOrderCoupons(OrderCouponParam param, CereBuyerUser user) throws CoBusinessException;

    void updateState(CereBuyerCoupon buyerCoupon) throws CoBusinessException;

    List<ProductCoupon> findCouponByProduct(BigDecimal price, Long buyerUserId, Long productId);

    List<ProductCoupon> getCouponList(CouponListParam param, CereBuyerUser user) throws CoBusinessException;

    CereBuyerCoupon findByCouponId(Long couponId,Long buyerUserId);

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;

    void updateByUserIdAndCouponId(CereBuyerCoupon cereBuyerCoupon) throws CoBusinessException;

    void takeBatchCoupon(List<CouponParam> paramList, CereBuyerUser user) throws CoBusinessException;

    CereBuyerCoupon findLatestUsedCoupon(Long couponId, Long buyerUserId);

    List<ProductCoupon> findCouponMatchCondition(Long buyerUserId, BigDecimal fullMoneyUpperLimit, List<Long> productIdList);
}
