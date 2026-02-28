/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.permission;

import com.shop.cereshop.commons.domain.permission.CerePlatformPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CerePlatformPermissionDAO extends BaseMapper<CerePlatformPermission> {
    int deleteByPrimaryKey(Long permissionId);

    int insertSelective(CerePlatformPermission record);

    CerePlatformPermission selectByPrimaryKey(Long permissionId);

    int updateByPrimaryKeySelective(CerePlatformPermission record);

    int updateByPrimaryKey(CerePlatformPermission record);
}
