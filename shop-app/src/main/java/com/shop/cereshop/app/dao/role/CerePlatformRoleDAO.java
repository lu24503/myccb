/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.role;

import com.shop.cereshop.commons.domain.role.CerePlatformRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CerePlatformRoleDAO extends BaseMapper<CerePlatformRole> {
    int deleteByPrimaryKey(Long roleId);

    int insertSelective(CerePlatformRole record);

    CerePlatformRole selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(CerePlatformRole record);

    int updateByPrimaryKey(CerePlatformRole record);

}
