/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop;

import com.shop.cereshop.app.page.comment.Comment;
import com.shop.cereshop.app.page.comment.CommentCount;
import com.shop.cereshop.app.page.product.BroadCastDTO;
import com.shop.cereshop.app.page.product.BuyerComment;
import com.shop.cereshop.app.param.comment.CommentParam;
import com.shop.cereshop.app.param.comment.CommentWorParam;
import com.shop.cereshop.app.param.comment.LikeParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.shop.CereShopComment;
import com.shop.cereshop.commons.domain.word.CerePlatformWord;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereShopCommentService {

    List<BuyerComment> findByProductId(Long productId);

    CereShopComment findShop(Long productId);

    void insert(CereShopComment cereShopComment) throws CoBusinessException;

    CommentCount getAll(CommentParam param, CereBuyerUser user) throws CoBusinessException;

    void like(LikeParam param, CereBuyerUser user) throws CoBusinessException;

    void update(CereShopComment cereShopComment) throws CoBusinessException;

    CereShopComment findByOrderId(Long orderId);

    List<CerePlatformWord> findWords();

    Comment getById(Long commentId) throws CoBusinessException;

    Page getProductAll(CommentWorParam param) throws CoBusinessException,Exception;

    int findProductNumber(Long orderId);

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;

    CereShopComment findComment(Long buyerUserId, Long orderId, Long skuId);

    CommentCount getCommentList(CommentParam param, CereBuyerUser user);

    int findCommentCount(Long buyerUserId);

    List<BroadCastDTO> findRecentComment(Long productId, String oneHourAgo);
}
