/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.groupwork;

import com.shop.cereshop.app.page.order.CollageDetail;
import com.shop.cereshop.app.page.order.CollagePerson;
import com.shop.cereshop.app.page.product.BroadCastDTO;
import com.shop.cereshop.commons.domain.collage.CereCollageOrder;
import com.shop.cereshop.commons.domain.collage.CollageOrder;
import com.shop.cereshop.commons.domain.order.CereShopOrder;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereCollageOrderService {
    List<CollageOrder> findCollageOrders(Long shopGroupWorkId,Long buyerUserId,Long productId);

    int findSpelled(Long collageId);

    void insert(CereCollageOrder cereCollageOrder) throws CoBusinessException;

    CereCollageOrder findByOrder(CereShopOrder order);

    int findSurplusNumber(Long collageId);

    void update(CereCollageOrder cereCollageOrder) throws CoBusinessException;

    List<Long> findNotPayCollageOrderIds(Long collageId, Long orderId);

    List<Long> findOrderIds(Long collageId);

    CollageDetail findDetail(Long shopGroupWorkId, Long orderId);

    List<CollagePerson> findPersons(Long collageId);

    CereCollageOrder findById(Long collageId);

    CereCollageOrder findByUserId(Long buyerUserId,Long orderId);

    List<CereShopOrder> findPayCollageOrderIds(Long collageId, Long orderId);

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;

    List<BroadCastDTO> findRecentCollageOrderGoing(String oneHourAgo, Long shopGroupWorkId, Long productId);

    List<BroadCastDTO> findRecentCollageOrderDone(String oneHourAgo, Long shopGroupWorkId, Long productId);
}
