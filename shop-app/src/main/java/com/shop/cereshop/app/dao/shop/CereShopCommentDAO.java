/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.shop;

import com.shop.cereshop.app.page.comment.Comment;
import com.shop.cereshop.app.page.comment.SelfComment;
import com.shop.cereshop.app.page.comment.WordComment;
import com.shop.cereshop.app.page.product.BroadCastDTO;
import com.shop.cereshop.app.page.product.BuyerComment;
import com.shop.cereshop.app.param.comment.CommentParam;
import com.shop.cereshop.app.param.comment.CommentWorParam;
import com.shop.cereshop.commons.domain.shop.CereShopComment;
import com.shop.cereshop.commons.domain.word.CerePlatformWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereShopCommentDAO extends BaseMapper<CereShopComment> {

    int insertSelective(CereShopComment record);

    List<BuyerComment> findByProductId(@Param("productId") Long productId);

    CereShopComment findShop(@Param("productId") Long productId);

    List<SelfComment> getAll(CommentParam param);

    Integer findTotal(@Param("buyerUserId") Long buyerUserId,@Param("state") Integer state);

    CereShopComment findByOrderId(@Param("orderId") Long orderId);

    List<CerePlatformWord> findWords();

    Comment getById(@Param("commentId") Long commentId);

    List<WordComment> getProductAll(CommentWorParam param);

    int findProductNumber(@Param("orderId") Long orderId);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);

    CereShopComment findComment(@Param("buyerUserId") Long buyerUserId,@Param("orderId") Long orderId,@Param("skuId") Long skuId);

    List<SelfComment> getCommentList(CommentParam param);

    List<SelfComment> getUnCommentList(CommentParam param);

    List<BroadCastDTO> findRecentComment(@Param("productId") Long productId,
                                         @Param("oneHourAgo") String oneHourAgo);
}
