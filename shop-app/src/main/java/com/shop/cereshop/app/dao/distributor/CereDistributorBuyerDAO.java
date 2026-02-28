/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.distributor;

import com.shop.cereshop.app.page.order.ShopDistributor;
import com.shop.cereshop.commons.domain.distributor.CereDistributorBuyer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CereDistributorBuyerDAO extends BaseMapper<CereDistributorBuyer> {

    int insertSelective(CereDistributorBuyer record);

    ShopDistributor findByUserId(@Param("buyerUserId") Long buyerUserId, @Param("shopId") Long shopId);

    CereDistributorBuyer findByDisAndUser(@Param("distributorId") Long distributorId,@Param("buyerUserId") Long buyerUserId);

    CereDistributorBuyer checkUser(@Param("distributorId") Long distributorId,@Param("buyerUserId") Long buyerUserId);

    void updateIfBind(CereDistributorBuyer distributorBuyer);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);
}
