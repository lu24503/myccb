/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.dao.coupon;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.param.coupon.ChannelCouponDetail;
import com.shop.cereshop.commons.domain.tool.CereChannelCoupon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CereChannelCouponDAO extends BaseMapper<CereChannelCoupon> {

    ChannelCouponDetail getChannelCouponDetail(CereChannelCoupon param);
}
