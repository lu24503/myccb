/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.distributor;

import com.shop.cereshop.commons.domain.distributor.CereShopDistributionLevel;
import com.shop.cereshop.commons.domain.distributor.CereShopDistributor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CereShopDistributionLevelDAO extends BaseMapper<CereShopDistributionLevel> {
    int deleteByPrimaryKey(Long distributorLevelId);

    int insertSelective(CereShopDistributionLevel record);

    CereShopDistributionLevel selectByPrimaryKey(Long distributorLevelId);

    int updateByPrimaryKeySelective(CereShopDistributionLevel record);

    int updateByPrimaryKey(CereShopDistributionLevel record);
}
