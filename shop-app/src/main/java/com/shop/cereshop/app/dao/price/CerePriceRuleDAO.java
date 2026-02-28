/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.price;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.commons.domain.permission.CerePlatformPermission;
import com.shop.cereshop.commons.domain.tool.CerePriceProduct;
import com.shop.cereshop.commons.domain.tool.CerePriceRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CerePriceRuleDAO extends BaseMapper<CerePlatformPermission> {

    int insertSelective(CerePriceRule record);

    List<CerePriceRule> findRules(Long priceId);

    List<CerePriceRule> findRulesByShopId(Long shopId);
}
