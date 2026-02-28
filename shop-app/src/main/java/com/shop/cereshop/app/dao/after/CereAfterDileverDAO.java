/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.after;

import com.shop.cereshop.commons.domain.after.CereAfterDilever;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CereAfterDileverDAO extends BaseMapper<CereAfterDilever> {
    int insertSelective(CereAfterDilever record);
}
