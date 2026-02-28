/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.groupwork.impl;

import com.shop.cereshop.app.dao.groupwork.CereCollageOrderDAO;
import com.shop.cereshop.app.page.order.CollageDetail;
import com.shop.cereshop.app.page.order.CollagePerson;
import com.shop.cereshop.app.page.product.BroadCastDTO;
import com.shop.cereshop.app.service.groupwork.CereCollageOrderService;
import com.shop.cereshop.commons.domain.collage.CereCollageOrder;
import com.shop.cereshop.commons.domain.collage.CollageOrder;
import com.shop.cereshop.commons.domain.order.CereShopOrder;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereCollageOrderServiceImpl implements CereCollageOrderService {

    @Autowired
    private CereCollageOrderDAO cereCollageOrderDAO;

    @Override
    public List<CollageOrder> findCollageOrders(Long shopGroupWorkId,Long buyerUserId,Long productId) {
        return cereCollageOrderDAO.findCollageOrders(shopGroupWorkId,buyerUserId,productId);
    }

    @Override
    public int findSpelled(Long collageId) {
        return cereCollageOrderDAO.findSpelled(collageId);
    }

    @Override
    public void insert(CereCollageOrder cereCollageOrder) throws CoBusinessException {
        cereCollageOrderDAO.insert(cereCollageOrder);
    }

    @Override
    public CereCollageOrder findByOrder(CereShopOrder order) {
        return cereCollageOrderDAO.findByOrder(order);
    }

    @Override
    public int findSurplusNumber(Long collageId) {
        return cereCollageOrderDAO.findSurplusNumber(collageId);
    }

    @Override
    public void update(CereCollageOrder cereCollageOrder) throws CoBusinessException {
        cereCollageOrderDAO.updateByPrimaryKeySelective(cereCollageOrder);
    }

    @Override
    public List<Long> findNotPayCollageOrderIds(Long collageId, Long orderId) {
        return cereCollageOrderDAO.findNotPayCollageOrderIds(collageId,orderId);
    }

    @Override
    public List<Long> findOrderIds(Long collageId) {
        return cereCollageOrderDAO.findOrderIds(collageId);
    }

    @Override
    public CollageDetail findDetail(Long shopGroupWorkId, Long orderId) {
        return cereCollageOrderDAO.findDetail(shopGroupWorkId,orderId);
    }

    @Override
    public List<CollagePerson> findPersons(Long collageId) {
        return cereCollageOrderDAO.findPersons(collageId);
    }

    @Override
    public CereCollageOrder findById(Long collageId) {
        return cereCollageOrderDAO.selectByPrimaryKey(collageId);
    }

    @Override
    public CereCollageOrder findByUserId(Long buyerUserId,Long orderId) {
        return cereCollageOrderDAO.findByUserId(buyerUserId,orderId);
    }

    @Override
    public List<CereShopOrder> findPayCollageOrderIds(Long collageId, Long orderId) {
        return cereCollageOrderDAO.findPayCollageOrderIds(collageId,orderId);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereCollageOrderDAO.updateBuyerData(buyerUserId,id);
    }

    @Override
    public List<BroadCastDTO> findRecentCollageOrderGoing(String oneHourAgo, Long shopGroupWorkId, Long productId) {
        return cereCollageOrderDAO.findRecentCollageOrderGoing(oneHourAgo, shopGroupWorkId, productId);
    }

    @Override
    public List<BroadCastDTO> findRecentCollageOrderDone(String oneHourAgo, Long shopGroupWorkId, Long productId) {
        return cereCollageOrderDAO.findRecentCollageOrderDone(oneHourAgo, shopGroupWorkId, productId);
    }
}
