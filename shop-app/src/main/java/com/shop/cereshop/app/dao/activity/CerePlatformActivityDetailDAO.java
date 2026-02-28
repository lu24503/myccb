/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.activity;

import com.shop.cereshop.commons.domain.activity.CereBuyerCoupon;
import com.shop.cereshop.commons.domain.activity.CerePlatformActivityDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CerePlatformActivityDetailDAO extends BaseMapper<CerePlatformActivityDetail> {
    int insertSelective(CerePlatformActivityDetail record);

    CereBuyerCoupon findByCouponId(@Param("couponId") Long couponId);
}
