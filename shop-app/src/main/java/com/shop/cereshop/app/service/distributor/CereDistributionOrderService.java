/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.distributor;

import com.shop.cereshop.app.page.distributor.Distributor;
import com.shop.cereshop.app.page.distributor.Reward;
import com.shop.cereshop.app.page.distributor.ShopDis;
import com.shop.cereshop.app.page.shop.Extension;
import com.shop.cereshop.app.param.distributor.DistributorOrderParam;
import com.shop.cereshop.app.param.distributor.DistributorParam;
import com.shop.cereshop.app.param.extension.ExtensionParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.distributor.CereDistributionOrder;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereDistributionOrderService {
    void insertBatch(List<CereDistributionOrder> distributionOrders) throws Exception;

    Distributor getDistributor(Long buyerUserId) throws CoBusinessException;

    Page getDistributorAll(PageParam param,Long buyerUserId) throws CoBusinessException;

    ShopDis getShopDistributor(DistributorParam param, CereBuyerUser user) throws CoBusinessException;

    Extension getShopExtension(ExtensionParam param, CereBuyerUser user) throws CoBusinessException,Exception;

    Page getDistributorOrderByShopId(DistributorOrderParam param) throws CoBusinessException;

    Page getDistributors(DistributorOrderParam param, CereBuyerUser user) throws CoBusinessException;

    Reward getReward(DistributorOrderParam param, CereBuyerUser user) throws CoBusinessException;

    Page getBuyers(DistributorParam param, CereBuyerUser user) throws CoBusinessException;

    Reward getNotReward(DistributorOrderParam param, CereBuyerUser user) throws CoBusinessException;

    void settleOrder(long parseLong);
}
