/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.sensitive;

import com.shop.cereshop.commons.domain.sensitive.CerePlatformSensitive;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CerePlatformSensitiveDAO extends BaseMapper<CerePlatformSensitive> {

    int insertSelective(CerePlatformSensitive record);

    CerePlatformSensitive findSensitive();
}
