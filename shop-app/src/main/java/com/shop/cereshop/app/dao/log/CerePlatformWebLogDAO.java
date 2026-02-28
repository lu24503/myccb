/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.log;

import com.shop.cereshop.commons.domain.log.CerePlatformWebLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CerePlatformWebLogDAO extends BaseMapper<CerePlatformWebLog> {
    int deleteByPrimaryKey(Long webLogId);

    int insertSelective(CerePlatformWebLog record);

    CerePlatformWebLog selectByPrimaryKey(Long webLogId);

    int updateByPrimaryKeySelective(CerePlatformWebLog record);

    int updateByPrimaryKey(CerePlatformWebLog record);
}
