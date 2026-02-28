/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.coupon;

import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.param.canvas.CanvasCouponParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.tool.CereShopCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereShopCouponDAO extends BaseMapper<CereShopCoupon> {
    int deleteByPrimaryKey(Long shopCouponId);

    int insertSelective(CereShopCoupon record);

    CereShopCoupon selectByPrimaryKey(Long shopCouponId);

    int updateByPrimaryKeySelective(CereShopCoupon record);

    int updateByPrimaryKey(CereShopCoupon record);

    List<ProductCoupon> getShopCoupons(CanvasCouponParam param);

    List<ProductCoupon> findProductCanUseCoupon(@Param("shopId") Long shopId,
                                                @Param("productId") Long productId);

    List<ProductCoupon> getShopCouponsByUserId(CanvasCouponParam param);

    List<CereShopCoupon> findOnGoingCouponByBatchId(List<Long> couponIdList);

    List<CereBuyerUser> selectCanTakeCouponUser(@Param("shopId") Long shopId, @Param("couponId") Long couponId,
                                                @Param("receiveType") Integer receiveType, @Param("frequency") Integer frequency);
}
