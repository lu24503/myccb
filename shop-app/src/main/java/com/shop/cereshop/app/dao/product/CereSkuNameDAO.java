/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.product;

import com.shop.cereshop.commons.domain.product.CereProductSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CereSkuNameDAO extends BaseMapper<CereProductSku> {
    int deleteByPrimaryKey(@Param("skuId") Long skuId);

    int insertSelective(CereProductSku record);

    int updateByPrimaryKeySelective(CereProductSku record);

    int updateByPrimaryKey(CereProductSku record);
}
