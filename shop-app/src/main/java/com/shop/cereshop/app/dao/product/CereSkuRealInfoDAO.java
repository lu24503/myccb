/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.dao.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.commons.domain.product.CereSkuRealInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereSkuRealInfoDAO extends BaseMapper<CereSkuRealInfo> {

    int updateSelective(@Param("realInfo") CereSkuRealInfo skuRealInfo,
                         @Param("resetSalesVolume") boolean resetSalesVolume);

    int increSalesVolumeBy(@Param("productId") Long productId,
                           @Param("addedNumber") Integer addedNumber);

    CereSkuRealInfo selectCombineMemberInfo(@Param("skuId") Long skuId,
                                            @Param("memberLevelId") Long memberLevelId);

    int updateSalesUserCount(List<Long> productIdList);
}
