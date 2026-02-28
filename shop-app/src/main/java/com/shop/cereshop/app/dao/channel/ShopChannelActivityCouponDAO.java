package com.shop.cereshop.app.dao.channel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.commons.domain.channel.ShopChannelActivityCoupon;
import com.shop.cereshop.commons.domain.tool.CereShopCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopChannelActivityCouponDAO extends BaseMapper<ShopChannelActivityCoupon> {

//    List<CereShopCoupon> selectChannelCouponByActivityId(@Param("channelActivityId") Long channelActivityId,
//                                                         @Param("shopId") Long shopId);
}
