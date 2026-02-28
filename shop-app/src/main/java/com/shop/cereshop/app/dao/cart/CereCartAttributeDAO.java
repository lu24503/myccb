/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.cart;

import com.shop.cereshop.commons.domain.cart.CereCartAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereCartAttributeDAO extends BaseMapper<CereCartAttribute> {

    int insertSelective(CereCartAttribute record);

    void insertBatch(@Param("list") List<CereCartAttribute> list);
}
