/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.business;

import com.shop.cereshop.commons.domain.business.CereBusinessUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CereBusinessUserRoleDAO extends BaseMapper<CereBusinessUserRole> {

    int insertSelective(CereBusinessUserRole record);
}
