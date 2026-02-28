/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.activity;

import com.shop.cereshop.commons.domain.activity.CereSignProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CereSignProductDAO extends BaseMapper<CereSignProduct> {
    int insertSelective(CereSignProduct record);

    int updateNumber(@Param("signType")Integer signType, @Param("activityId")Long activityId,
                     @Param("productId")Long productId, @Param("stockNumber")int stockNumber);
}
