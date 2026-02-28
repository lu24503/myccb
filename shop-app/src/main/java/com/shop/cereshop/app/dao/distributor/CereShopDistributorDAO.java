/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.distributor;

import com.shop.cereshop.app.page.order.ShopDistributor;
import com.shop.cereshop.commons.domain.distributor.CereShopDistributor;
import com.shop.cereshop.commons.domain.shop.CereShopRecruit;
import com.shop.cereshop.commons.domain.shop.CereShopRelationship;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CereShopDistributorDAO extends BaseMapper<CereShopDistributor> {
    int deleteByPrimaryKey(Long distributorId);

    int insertSelective(CereShopDistributor record);

    CereShopDistributor selectByPrimaryKey(Long distributorId);

    int updateByPrimaryKeySelective(CereShopDistributor record);

    int updateByPrimaryKey(CereShopDistributor record);

    ShopDistributor  findByPhone(@Param("phone") String phone,@Param("shopId") Long shopId);

    Long findMinLevel(@Param("shopId") Long shopId);

    CereShopDistributor findInvitees(@Param("invitationCode") String invitationCode);

    String findInvitationCode(@Param("distributorId") Long distributorId);

    CereShopDistributor check(@Param("shopId") Long shopId,@Param("buyerUserId") Long buyerUserId);

    CereShopRelationship findRelation(@Param("shopId") Long shopId);

    CereShopDistributor checkPhone(@Param("phone") String phone,@Param("shopId") Long shopId);

    CereShopRecruit findShopRecruit(@Param("shopId") Long shopId);
}
