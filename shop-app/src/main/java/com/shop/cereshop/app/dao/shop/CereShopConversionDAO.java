/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.shop;

import com.shop.cereshop.commons.domain.shop.CereShopConversion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereShopConversionDAO extends BaseMapper<CereShopConversion> {
    int deleteByPrimaryKey(Long shopId);

    int insertSelective(CereShopConversion record);

    CereShopConversion selectByPrimaryKey(Long shopId);

    int updateByPrimaryKeySelective(CereShopConversion record);

    int updateByPrimaryKey(CereShopConversion record);

    void insertBatch(@Param("list") List<CereShopConversion> list);
}
