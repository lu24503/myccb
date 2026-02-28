/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.coupon;

import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.param.canvas.CanvasCouponParam;
import com.shop.cereshop.app.param.coupon.ShopCouponParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.tool.CereShopCoupon;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereShopCouponService {
    List<ProductCoupon> findByProductId(Long shopId, Long productId) ;

    void takeCoupon(ShopCouponParam param, CereBuyerUser user) throws CoBusinessException;

    CereShopCoupon findById(Long shopCouponId);

    void updateByPrimaryKeySelective(CereShopCoupon cereShopCoupon) throws CoBusinessException;

    Page getShopCoupons(CanvasCouponParam param,CereBuyerUser user) throws CoBusinessException;

    /**
     * 查询进行中的优惠券，如果指定商品可用，或指定商品不可用，需满足包含指定条件中的商品id,
     * 已达到领取上限或者库存为0的都不查询出来
     * @param buyerUserId
     * @param shopId
     * @param productId
     * @return
     */
    List<ProductCoupon> findByProductIdAndUserId(Long buyerUserId, Long shopId, Long productId);

    List<CereShopCoupon> findOnGoingCouponByBatchId(List<Long> couponIdList);

    List<CereBuyerUser> selectCanTakeCouponUser(Long shopId, Long couponId, Integer receiveType, Integer frequency);
}
