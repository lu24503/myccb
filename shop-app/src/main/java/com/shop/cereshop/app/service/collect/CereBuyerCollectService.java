/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.collect;

import com.shop.cereshop.app.param.collect.CollectGetAllParam;
import com.shop.cereshop.app.param.collect.CollectIdParam;
import com.shop.cereshop.app.param.collect.CollectParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.collect.CereBuyerCollect;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CereBuyerCollectService {
    void collect(CollectParam param, CereBuyerUser user) throws CoBusinessException;

    CereBuyerCollect findByUserProduct(Long buyerUserId, Long productId);

    CereBuyerCollect findByUserShopId(Long buyerUserId, Long shopId);

    void cancel(CollectIdParam param, CereBuyerUser user) throws CoBusinessException;

    void newCancel(CollectParam param, CereBuyerUser user) throws CoBusinessException;

    Page getAllProduct(CollectGetAllParam param, CereBuyerUser user) throws CoBusinessException;

    Page getAllShop(CollectGetAllParam param, CereBuyerUser user) throws CoBusinessException;

    void selected(CollectIdParam param, CereBuyerUser user) throws CoBusinessException;

    void delete(CollectIdParam param, CereBuyerUser user) throws CoBusinessException;

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;

    int findShopCollectCount(Long shopId);

    int findAllCollectCount(Long buyerUserId);
}
