/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.activity;

import com.shop.cereshop.commons.domain.activity.CereActivitySign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CereActivitySignDAO extends BaseMapper<CereActivitySign> {
    int deleteByPrimaryKey(Long signId);

    int insertSelective(CereActivitySign record);

    CereActivitySign selectByPrimaryKey(Long signId);

    int updateByPrimaryKeySelective(CereActivitySign record);

    int updateByPrimaryKey(CereActivitySign record);
}
