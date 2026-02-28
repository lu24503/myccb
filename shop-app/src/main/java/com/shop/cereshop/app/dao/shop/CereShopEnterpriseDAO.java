/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.shop;

import com.shop.cereshop.app.param.shop.CereShopEnterpriseParam;
import com.shop.cereshop.commons.domain.shop.CereShopEnterprise;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CereShopEnterpriseDAO extends BaseMapper<CereShopEnterprise> {

    int insertSelective(CereShopEnterprise record);

    void updateEnterprise(CereShopEnterprise enterprise);

    CereShopEnterprise findByShopId(@Param("shopId") Long shopId);

    void insertParam(CereShopEnterpriseParam param);

    void updateParam(CereShopEnterpriseParam param);
}
