/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.pay;

import com.shop.cereshop.commons.domain.pay.CerePayLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CerePayLogDAO extends BaseMapper<CerePayLog> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(CerePayLog record);

    CerePayLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CerePayLog record);

    int updateByPrimaryKey(CerePayLog record);
}
