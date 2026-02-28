package com.shop.cereshop.app.dao.coupon;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.coupon.CommonCoupon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CereCommonCouponDAO extends BaseMapper<CommonCoupon> {

    List<CommonCoupon> selectCouponList();

    List<CommonCoupon> getChannelActivityCoupon(Long activityId);
}
