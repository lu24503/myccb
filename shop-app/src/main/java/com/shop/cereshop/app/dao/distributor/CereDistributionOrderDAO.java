/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.distributor;

import com.shop.cereshop.app.page.distributor.*;
import com.shop.cereshop.app.param.distributor.DistributorOrderParam;
import com.shop.cereshop.app.param.distributor.DistributorParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerWithdrawal;
import com.shop.cereshop.commons.domain.distributor.CereDistributionOrder;
import com.shop.cereshop.commons.domain.distributor.CereShopDistributor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CereDistributionOrderDAO extends BaseMapper<CereDistributionOrder> {
    int deleteByPrimaryKey(Long orderId);

    int insertSelective(CereDistributionOrder record);

    CereDistributionOrder selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(CereDistributionOrder record);

    int updateByPrimaryKey(CereDistributionOrder record);

    void insertBatch(@Param("list") List<CereDistributionOrder> list);

    List<CereShopDistributor> findDistributorByUserId(@Param("buyerUserId") Long buyerUserId);

    BigDecimal findTotalByDistributorId(@Param("distributorId") Long distributorId);

    BigDecimal findWithdrawalMoney(@Param("buyerUserId") Long buyerUserId);

    List<CereBuyerWithdrawal> findHistory(@Param("buyerUserId") Long buyerUserId);

    List<DistributorShop> getDistributorAll(@Param("buyerUserId") Long buyerUserId);

    BigDecimal findTotalByShopIdAndDistributor(DistributorShop distributorShop);

    BigDecimal findCumulativeByDistributorId(@Param("shopId") Long shopId, @Param("distributorId") Long distributorId, @Param("state") Integer state);

    Integer findUsers(@Param("shopId") Long shopId, @Param("distributorId") Long distributorId);

    Integer findDistributors(@Param("shopId") Long shopId, @Param("distributorId") Long distributorId);

    List<DistributorOrder> getDistributorOrderByShopId(DistributorOrderParam param);

    List<CumulativeDistributor> getDistributors(@Param("curDistributorId") Long distributorId, @Param("distributorIdList") List<Long> distributorIdList);

    List<CumulativeReward> getReward(DistributorOrderParam param);

    BigDecimal findPriceByShopIdAndType(@Param("shopId") Long shopId, @Param("distributorId") Long distributorId, @Param("type") Integer type);

    List<Long> getBuyersIdList(DistributorParam param);

    List<CumulativeBuyer> getBuyers(@Param("shopId") Long shopId, @Param("buyerIdList") List<Long> buyerIdList);

    List<CumulativeReward> getNotReward(DistributorOrderParam param);

    BigDecimal findPriceByShopIdAndTypeAndState(@Param("shopId") Long shopId, @Param("distributorId") Long distributorId, @Param("type") Integer type);

    List<Long> getSubDistributors(DistributorOrderParam param);

    void settleOrder(@Param("orderId") long orderId);
}
