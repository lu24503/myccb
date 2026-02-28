/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer.impl;

import com.shop.cereshop.app.dao.buyer.CereBuyerCommentLikeDAO;
import com.shop.cereshop.app.service.buyer.CereBuyerCommentLikeService;
import com.shop.cereshop.commons.domain.buyer.CereBuyerCommentLike;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CereBuyerCommentLikeServiceImpl implements CereBuyerCommentLikeService {

    @Autowired
    private CereBuyerCommentLikeDAO cereBuyerCommentLikeDAO;

    @Override
    public CereBuyerCommentLike findByUserIdAndCommentId(Long buyerUserId, Long commentId) {
        return cereBuyerCommentLikeDAO.findByUserIdAndCommentId(buyerUserId,commentId);
    }

    @Override
    public void insert(CereBuyerCommentLike like) throws CoBusinessException {
        cereBuyerCommentLikeDAO.insert(like);
    }

    @Override
    public void update(CereBuyerCommentLike like) throws CoBusinessException {
        cereBuyerCommentLikeDAO.updateByPrimaryKeySelective(like);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereBuyerCommentLikeDAO.updateBuyerData(buyerUserId,id);
    }

    @Override
    public void insertOrUpdate(Long buyerUserId, Long commentId, Integer ifLike) {
        cereBuyerCommentLikeDAO.insertOrUpdate(buyerUserId, commentId, ifLike);
    }
}
