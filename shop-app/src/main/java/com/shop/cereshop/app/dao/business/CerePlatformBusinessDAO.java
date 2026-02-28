/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.business;

import com.shop.cereshop.commons.domain.business.CerePlatformBusiness;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CerePlatformBusinessDAO extends BaseMapper<CerePlatformBusiness> {
    int deleteByPrimaryKey(Long businessUserId);

    int insertSelective(CerePlatformBusiness record);

    CerePlatformBusiness selectByPrimaryKey(Long businessUserId);

    int updateByPrimaryKeySelective(CerePlatformBusiness record);

    int updateByPrimaryKey(CerePlatformBusiness record);

    CerePlatformBusiness checkUserName(@Param("shopPhone") String shopPhone);

    CerePlatformBusiness checkPhone(String phone);
}
