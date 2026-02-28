/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.distributor;

import com.shop.cereshop.app.page.order.ShopDistributor;
import com.shop.cereshop.app.param.distributor.ShopDistributorParam;
import com.shop.cereshop.app.param.extension.ExtensionParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.distributor.CereShopDistributor;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CereShopDistributorService {
    ShopDistributor  findByPhone(String phone,Long shopId);

    void addDistributor(ShopDistributorParam param) throws CoBusinessException;

    String findInvitationCode(Long distributorId);

    CereShopDistributor check(Long shopId, Long buyerUserId) throws CoBusinessException;

    void bind(ExtensionParam param, CereBuyerUser user) throws CoBusinessException,Exception;

    String getInvitationCode(Long shopId, String phone);
}
