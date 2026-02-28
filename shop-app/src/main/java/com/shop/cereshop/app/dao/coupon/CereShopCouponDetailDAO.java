/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.coupon;

import com.shop.cereshop.commons.domain.tool.CereShopCouponDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CereShopCouponDetailDAO extends BaseMapper<CereShopCouponDetail> {

    int insertSelective(CereShopCouponDetail record);
}
