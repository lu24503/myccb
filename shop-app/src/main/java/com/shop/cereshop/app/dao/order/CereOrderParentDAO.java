/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.order;

import com.shop.cereshop.commons.domain.order.CereOrderParent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereOrderParentDAO extends BaseMapper<CereOrderParent> {
    int deleteByPrimaryKey(Long parentId);

    int insertSelective(CereOrderParent record);

    CereOrderParent selectByPrimaryKey(Long parentId);

    int updateByPrimaryKeySelective(CereOrderParent record);

    int updateByPrimaryKey(CereOrderParent record);

    CereOrderParent findById(@Param("id") Long id);

    List<Long> findShopIds(@Param("id") Long id);
}
