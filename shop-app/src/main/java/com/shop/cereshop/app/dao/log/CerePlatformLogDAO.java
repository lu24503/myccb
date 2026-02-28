/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.log;

import com.shop.cereshop.commons.domain.log.CerePlatformLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CerePlatformLogDAO extends BaseMapper<CerePlatformLog> {
    int deleteByPrimaryKey(Long logId);

    int insertSelective(CerePlatformLog record);

    CerePlatformLog selectByPrimaryKey(Long logId);

    int updateByPrimaryKeySelective(CerePlatformLog record);

    int updateByPrimaryKey(CerePlatformLog record);
}
