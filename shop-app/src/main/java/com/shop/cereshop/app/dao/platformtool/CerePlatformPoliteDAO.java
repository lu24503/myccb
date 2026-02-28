/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.platformtool;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformPolite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CerePlatformPoliteDAO extends BaseMapper<CerePlatformPolite> {
    int deleteByPrimaryKey(Long politeId);

    CerePlatformPolite selectByPrimaryKey(Long politeId);

    CerePlatformPolite selectOnGoingPolite();
}
