/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.groupwork;

import com.shop.cereshop.app.domain.activity.ActivityData;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.commons.domain.tool.CereShopGroupWorkDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CereShopGroupWorkDetailDAO extends BaseMapper<CereShopGroupWorkDetail> {

    int insertSelective(CereShopGroupWorkDetail record);

    ActivityData findPriceBySkuId(@Param("skuId") Long skuId);

    List<ToolProduct> findDistinctProducts(@Param("shopGroupWorkId") Long shopGroupWorkId);

    BigDecimal findPriceByGroupWorkIdAndSkuId(@Param("shopGroupWorkId") Long shopGroupWorkId, @Param("skuId") Long skuId);

    Long findSkuIdByProductId(Long productId);
}
