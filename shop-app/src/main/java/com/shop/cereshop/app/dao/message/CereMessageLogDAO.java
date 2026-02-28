/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.message;

import com.shop.cereshop.commons.domain.message.CereMessageLog;
import com.shop.cereshop.commons.domain.message.CerePlatfromMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CereMessageLogDAO extends BaseMapper<CereMessageLog> {

    int insertSelective(CereMessageLog record);

    CerePlatfromMessage findMessage(@Param("template") String template);
}
