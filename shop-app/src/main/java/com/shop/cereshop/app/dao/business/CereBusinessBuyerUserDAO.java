/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.commons.domain.business.CereBusinessBuyerUser;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CereBusinessBuyerUserDAO extends BaseMapper<CereBusinessBuyerUser> {

    int insertOrUpdate(CereBusinessBuyerUser cereBusinessBuyerUser);

    int refreshUpdateTime(CereBusinessBuyerUser bbu);

    List<CereBuyerUser> selectBuyerUserByShopId(Long shopId);
}
