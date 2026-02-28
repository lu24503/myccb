/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.express;

import com.shop.cereshop.commons.domain.express.CerePlatformExpress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CerePlatformExpressDAO extends BaseMapper<CerePlatformExpress> {

    int insertSelective(CerePlatformExpress record);

    Integer findExpress();
}
