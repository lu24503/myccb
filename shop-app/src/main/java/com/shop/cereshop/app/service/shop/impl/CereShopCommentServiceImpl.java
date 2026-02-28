/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.shop.CereShopCommentDAO;
import com.shop.cereshop.app.page.comment.Comment;
import com.shop.cereshop.app.page.comment.CommentCount;
import com.shop.cereshop.app.page.comment.SelfComment;
import com.shop.cereshop.app.page.comment.WordComment;
import com.shop.cereshop.app.page.product.BroadCastDTO;
import com.shop.cereshop.app.page.product.BuyerComment;
import com.shop.cereshop.app.param.comment.CommentParam;
import com.shop.cereshop.app.param.comment.CommentWorParam;
import com.shop.cereshop.app.param.comment.LikeParam;
import com.shop.cereshop.app.service.buyer.CereBuyerCommentLikeService;
import com.shop.cereshop.app.service.shop.CereShopCommentService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.shop.CereShopComment;
import com.shop.cereshop.commons.domain.word.CerePlatformWord;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CereShopCommentServiceImpl implements CereShopCommentService {

    @Autowired
    private CereShopCommentDAO cereShopCommentDAO;

    @Autowired
    private CereBuyerCommentLikeService cereBuyerCommentLikeService;

    @Override
    public List<BuyerComment> findByProductId(Long productId) {
        return cereShopCommentDAO.findByProductId(productId);
    }

    @Override
    public CereShopComment findShop(Long productId) {
        return cereShopCommentDAO.findShop(productId);
    }

    @Override
    public void insert(CereShopComment cereShopComment) throws CoBusinessException {
        cereShopCommentDAO.insert(cereShopComment);
    }

    @Override
    public CommentCount getAll(CommentParam param, CereBuyerUser user) throws CoBusinessException {
        CommentCount commentCount=new CommentCount();
        //查询全部评价数量
        commentCount.setTotal(cereShopCommentDAO.findTotal(user.getBuyerUserId(),null));
        //查询有图评价数量
        commentCount.setImageTotal(cereShopCommentDAO.findTotal(user.getBuyerUserId(), IntegerEnum.YES.getCode()));
        param.setBuyerUserId(user.getBuyerUserId());
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<SelfComment> list=cereShopCommentDAO.getAll(param);
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(selfComment -> {
                if(!EmptyUtils.isEmpty(selfComment.getAddTime())){
                    selfComment.setIfAdd(IntegerEnum.YES.getCode());
                }else {
                    selfComment.setIfAdd(IntegerEnum.NO.getCode());
                }
                //设置规格属性值数组
                selfComment.setValues(EmptyUtils.getImages(selfComment.getValue()));
            });
        }
        PageInfo<SelfComment> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        commentCount.setPage(page);
        return commentCount;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void like(LikeParam param, CereBuyerUser user) throws CoBusinessException {
        cereBuyerCommentLikeService.insertOrUpdate(user.getBuyerUserId(), param.getCommentId(), param.getIfLike());
    }

    @Override
    public void update(CereShopComment cereShopComment) throws CoBusinessException {
        cereShopCommentDAO.updateById(cereShopComment);
    }

    @Override
    public CereShopComment findByOrderId(Long orderId) {
        return cereShopCommentDAO.findByOrderId(orderId);
    }

    @Override
    public List<CerePlatformWord> findWords() {
        return cereShopCommentDAO.findWords();
    }

    @Override
    public Comment getById(Long commentId) throws CoBusinessException {
        Comment comment = cereShopCommentDAO.getById(commentId);
        if(comment!=null&&!EmptyUtils.isEmpty(comment.getImage())){
            comment.setImages(EmptyUtils.getImages(comment.getImage()));
        }
        return comment;
    }

    @Override
    public Page getProductAll(CommentWorParam param) throws CoBusinessException,Exception {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<WordComment> list=cereShopCommentDAO.getProductAll(param);
        if(!EmptyUtils.isEmpty(list)){
            for (WordComment wordComment:list) {
                //计算追评时间和评价时间之间天数
                if (!EmptyUtils.isEmpty(wordComment.getAddTime())) {
                    wordComment.setDay(TimeUtils.differentDaysByMillisecond(wordComment.getCreateTime(), wordComment.getAddTime()));
                }
                //设置图片数组
                if (!EmptyUtils.isEmpty(wordComment.getImage())) {
                    wordComment.setImages(EmptyUtils.getImages(wordComment.getImage()));
                }
                if (!EmptyUtils.isEmpty(wordComment.getAddImage())) {
                    wordComment.setAddImages(EmptyUtils.getImages(wordComment.getAddImage()));
                }
            }
        }
        PageInfo<WordComment> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public int findProductNumber(Long orderId) {
        return cereShopCommentDAO.findProductNumber(orderId);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereShopCommentDAO.updateBuyerData(buyerUserId,id);
    }

    @Override
    public CereShopComment findComment(Long buyerUserId, Long orderId, Long skuId) {
        return cereShopCommentDAO.findComment(buyerUserId, orderId, skuId);
    }

    @Override
    public CommentCount getCommentList(CommentParam param, CereBuyerUser user) {
        CommentCount commentCount=new CommentCount();
        param.setBuyerUserId(user.getBuyerUserId());
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<SelfComment> list = null;
        if (param.getType() == 1) {
            list=cereShopCommentDAO.getUnCommentList(param);
        } else {
            list=cereShopCommentDAO.getCommentList(param);
            if(!EmptyUtils.isEmpty(list)){
                list.forEach(selfComment -> {
                    if(!EmptyUtils.isEmpty(selfComment.getAddTime())){
                        selfComment.setIfAdd(IntegerEnum.YES.getCode());
                    }else {
                        selfComment.setIfAdd(IntegerEnum.NO.getCode());
                    }
                    //设置规格属性值数组
                    selfComment.setValues(EmptyUtils.getImages(selfComment.getValue()));
                });
            }
        }
        PageInfo<SelfComment> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        commentCount.setPage(page);
        return commentCount;
    }

    @Override
    public int findCommentCount(Long buyerUserId) {
        return cereShopCommentDAO.selectCount(Wrappers.<CereShopComment>lambdaQuery().eq(CereShopComment::getBuyerUserId, buyerUserId));
    }

    @Override
    public List<BroadCastDTO> findRecentComment(Long productId, String oneHourAgo) {
        return cereShopCommentDAO.findRecentComment(productId, oneHourAgo);
    }
}
