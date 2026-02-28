/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.distributor;

import com.shop.cereshop.app.page.order.ShopDistributor;
import com.shop.cereshop.commons.domain.distributor.CereDistributorBuyer;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.apache.ibatis.annotations.Param;

public interface CereDistributorBuyerService {
    ShopDistributor findByUserId(Long buyerUserId, Long shopId);

    CereDistributorBuyer findByDisAndUser(Long distributorId,Long buyerUserId);

    CereDistributorBuyer checkUser(Long distributorId, Long buyerUserId);

    void update(CereDistributorBuyer distributorBuyer) throws CoBusinessException;

    void insert(CereDistributorBuyer cereDistributorBuyer) throws CoBusinessException;

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;
}
