/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.groupwork;

import com.shop.cereshop.app.page.groupwork.GroupInvite;
import com.shop.cereshop.app.page.groupwork.GroupWorkIndex;
import com.shop.cereshop.app.page.groupwork.ShareQrcode;
import com.shop.cereshop.app.page.groupwork.ShopGroupWorkUDetail;
import com.shop.cereshop.app.page.product.BroadCastDTO;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.page.settlement.Settlement;
import com.shop.cereshop.app.param.groupwork.CollageIdParam;
import com.shop.cereshop.app.param.groupwork.GroupWorkParam;
import com.shop.cereshop.app.param.groupwork.GroupWorkSettlementParam;
import com.shop.cereshop.app.param.groupwork.ShareQrcodeParam;
import com.shop.cereshop.app.param.product.ProductParam;
import com.shop.cereshop.app.param.renovation.RenovationParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.tool.CereShopGroupWork;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereShopGroupWorkService {
    GroupWorkIndex getIndex(GroupWorkParam param, CereBuyerUser user) throws CoBusinessException,Exception;

    @Deprecated
    ProductDetail getById(CereBuyerUser user, ProductParam param,ProductDetail detail) throws CoBusinessException,Exception;

    Settlement getSettlement(GroupWorkSettlementParam param, CereBuyerUser user) throws CoBusinessException;

    CereShopGroupWork findById(Long shopGroupWorkId);

    Long findByProductId(Long skuId);

    GroupInvite getInvite(CollageIdParam param, CereBuyerUser user) throws CoBusinessException,Exception;

    ShareQrcode getShare(ShareQrcodeParam param) throws CoBusinessException,Exception;

    List<ShopGroupWorkUDetail> getGroupWorks(RenovationParam param);

    ProductDetail setActivityInfo(Long shopGroupWorkId, CereBuyerUser user, ProductParam param, ProductDetail detail) throws Exception;

    List<BroadCastDTO> findCollageList(Long buyerUserId, Long shopGroupWorkId, String oneHourAgo);

    ProductDetail setActivityInfoForRealInfo(Long shopGroupWorkId, CereBuyerUser user, ProductParam param, ProductDetail detail) throws Exception;
}
