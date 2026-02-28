/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.buyer;

import com.shop.cereshop.app.page.coupon.CommonCoupon;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.param.order.OrderProductParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerShopCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CereBuyerShopCouponDAO extends BaseMapper<CereBuyerShopCoupon> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(CereBuyerShopCoupon record);

    CereBuyerShopCoupon selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CereBuyerShopCoupon record);

    int updateByPrimaryKey(CereBuyerShopCoupon record);

    void insertBatch(@Param("list") List<CereBuyerShopCoupon> list);

    List<ProductCoupon> findCouponByProduct(@Param("total") BigDecimal total,@Param("buyerUserId") Long buyerUserId,@Param("productId") Long productId);

    CereBuyerShopCoupon findById(@Param("id") Long id);

    void updateState(CereBuyerShopCoupon cereBuyerShopCoupon);

    int findCount(@Param("buyerUserId") Long buyerUserId,@Param("shopCouponId") Long shopCouponId);

    List<ProductCoupon> getCoupons(@Param("buyerUserId") Long buyerUserId,@Param("state") Integer state);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);

    List<Long> findProductIds(@Param("shopCouponId") Long shopCouponId);

    List<CereBuyerShopCoupon> findByIds(@Param("list") List<OrderProductParam> list);

    List<ProductCoupon> findCouponMatchCondition(@Param("buyerUserId") Long buyerUserId,
                                                 @Param("fullMoneyUpperLimit") BigDecimal fullMoneyUpperLimit,
                                                 @Param("productIdList") List<Long> productIdList,
                                                 @Param("nowTime") String nowTime);

    List<CommonCoupon> selectTakeCount(@Param("buyerUserId") Long buyerUserId,
                                       @Param("couponIdList") List<Long> couponIdList);
}
