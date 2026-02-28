/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer;

import com.shop.cereshop.app.page.coupon.CommonCoupon;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.param.order.OrderProductParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerShopCoupon;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.math.BigDecimal;
import java.util.List;

public interface CereBuyerShopCouponService {
    List<ProductCoupon> findCouponByProduct(BigDecimal total, Long buyerUserId, Long productId);

    CereBuyerShopCoupon findById(Long id);

    void updateState(CereBuyerShopCoupon cereBuyerShopCoupon) throws CoBusinessException;

    void insert(CereBuyerShopCoupon cereBuyerShopCoupon) throws CoBusinessException;

    int findCount(Long buyerUserId, Long shopCouponId);

    List<ProductCoupon> getCoupons(Long buyerUserId, Integer state);

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;

    List<Long> findProductIds(Long shopCouponId);

    List<CereBuyerShopCoupon> findByIds(List<OrderProductParam> shops);

    List<ProductCoupon> findCouponMatchCondition(Long buyerUserId, BigDecimal fullMoneyUpperLimit, List<Long> productIdList);

    /**
     * 该方法只查询用户 在这这批优惠券中 每个券领取多少次，不查询其它信息
     * @param buyerUserId
     * @param couponIdList
     * @return
     */
    List<CommonCoupon> selectTakeCount(Long buyerUserId, List<Long> couponIdList);
}
