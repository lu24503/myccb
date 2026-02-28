package com.shop.cereshop.app.dao.channel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.commons.domain.channel.ShopChannelActivity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopChannelActivityDAO extends BaseMapper<ShopChannelActivity> {

    int updateCheckRemainCount(Long shopChannelActivityId);

}
