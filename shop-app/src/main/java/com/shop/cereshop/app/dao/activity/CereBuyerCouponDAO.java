/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.coupon.CommonCoupon;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.param.coupon.ActivityParam;
import com.shop.cereshop.commons.domain.activity.CereBuyerCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CereBuyerCouponDAO extends BaseMapper<CereBuyerCoupon> {

    //int insertSelective(CereBuyerCoupon record);

    Integer findByActivity(ProductCoupon tool);

    List<ProductCoupon> getCoupons(@Param("buyerUserId") Long buyerUserId,@Param("state") Integer state);

    List<Product> getCouponProducts(ActivityParam param);

    List<ProductCoupon> findCouponByProduct(@Param("price") BigDecimal price,
                                      @Param("buyerUserId") Long buyerUserId,@Param("productId") Long productId);

    void updateState(CereBuyerCoupon buyerCoupon);

    CereBuyerCoupon findByCouponId(@Param("couponId") Long couponId,@Param("buyerUserId") Long buyerUserId);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);

    List<Product> getShopCouponProducts(ActivityParam param);

    void updateByUserIdAndCouponId(CereBuyerCoupon cereBuyerCoupon);

    int findCount(@Param("buyerUserId") Long buyerUserId,@Param("activityId") Long activityId);

    CereBuyerCoupon findLatestUsedCoupon(@Param("couponId") Long couponId,@Param("buyerUserId") Long buyerUserId);

    List<ProductCoupon> findCouponMatchCondition(@Param("buyerUserId") Long buyerUserId,
                                                 @Param("fullMoneyUpperLimit") BigDecimal fullMoneyUpperLimit,
                                                 @Param("productIdList") List<Long> productIdList,
                                                 @Param("nowTime") String nowTime);

    List<CommonCoupon> selectTakeCount(@Param("buyerUserId") Long buyerUserId,
                                       @Param("couponIdList") List<Long> couponIdList);
}
