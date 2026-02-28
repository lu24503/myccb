/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.notice.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.notice.CereBuyerNoticeDAO;
import com.shop.cereshop.app.dao.notice.CereNoticeDAO;
import com.shop.cereshop.app.page.notice.BuyerNotice;
import com.shop.cereshop.app.page.notice.NoticeDetail;
import com.shop.cereshop.app.param.notice.NoticeIdParam;
import com.shop.cereshop.app.service.notice.CereNoticeService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.notice.CereBuyerNotice;
import com.shop.cereshop.commons.domain.notice.CereNotice;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class CereNoticeServiceImpl implements CereNoticeService {

    @Autowired
    private CereNoticeDAO cereNoticeDAO;

    @Autowired
    private CereBuyerNoticeDAO cereBuyerNoticeDAO;

    @Override
    public Page getAll(PageParam pageParam,CereBuyerUser user) throws CoBusinessException {
        PageHelper.startPage(pageParam.getPage(),pageParam.getPageSize());
        Long buyerUserId=null;
        String createTime =null;
        if(user!=null){
            buyerUserId=user.getBuyerUserId();
            createTime = user.getCreateTime();
        }
        List<BuyerNotice> list=cereNoticeDAO.getAll(buyerUserId,createTime);
        PageInfo<BuyerNotice> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public NoticeDetail getById(NoticeIdParam param) throws CoBusinessException {
        return cereNoticeDAO.getById(param.getNoticeId());
    }

    @Override
    public List<BuyerNotice> getGongGaoAll(CereBuyerUser user) throws CoBusinessException {
        Long buyerUserId=null;
        if(user!=null){
            buyerUserId=user.getBuyerUserId();
        }
        return cereNoticeDAO.getGongGaoAll(buyerUserId);
    }

    @Override
    public void insert(CereNotice cereNotice) throws CoBusinessException {
        cereNoticeDAO.insert(cereNotice);
    }

    @Override
    public void insertBatch(List<CereNotice> notices) throws CoBusinessException {
        cereNoticeDAO.insertBatch(notices);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereNoticeDAO.updateBuyerData(buyerUserId,id);
    }

    @Override
    public int getNotRead(Long buyerUserId,String createTime) {
        return cereNoticeDAO.getNotRead(buyerUserId,createTime);
    }

    @Override
    public void update(CereNotice cereNotice) throws CoBusinessException {
        cereNoticeDAO.updateByPrimaryKeySelective(cereNotice);
    }

    @Override
    public int getRead(Long buyerUserId) {
        return cereNoticeDAO.getRead(buyerUserId);
    }

    @Override
    public List<Long> findIdsByOrderId(Long orderId) {
        return cereNoticeDAO.findIdsByOrderId(orderId);
    }

    @Override
    public void deleteByIds(List<Long> noticeIds) throws CoBusinessException {
        cereNoticeDAO.deleteByIds(noticeIds);
    }

    @Override
    public void deleteBuyerNotice(List<Long> noticeIds, Long buyerUserId) throws CoBusinessException {
        cereNoticeDAO.deleteBuyerNotice(noticeIds,buyerUserId);
    }

    @Override
    public List<BuyerNotice> getNotices() {
        return cereNoticeDAO.getNotices();
    }

    @Override
    public void readAll(Long buyerUserId) {
        cereNoticeDAO.readAll(buyerUserId);
        List<BuyerNotice> notReadMessageList = cereNoticeDAO.selectNotReadMessage(buyerUserId);
        for (BuyerNotice notice:notReadMessageList) {
            CereBuyerNotice buyerNotice = new CereBuyerNotice();
            buyerNotice.setBuyerUserId(buyerUserId);
            buyerNotice.setNoticeId(notice.getNoticeId());
            cereBuyerNoticeDAO.insert(buyerNotice);
        }
    }

    @Override
    public void removeById(Long noticeId, Long buyerUserId) {
        CereNotice notice = cereNoticeDAO.selectByPrimaryKey(noticeId);
        if (notice != null && IntegerEnum.NOTICE_TYPE_SYSTEM.getCode().equals(notice.getNoticeType())) {
            cereNoticeDAO.deleteBuyerNotice(Collections.singletonList(noticeId), buyerUserId);
        } else {
            CereBuyerNotice buyerNotice = cereBuyerNoticeDAO.selectBuyerNotice(noticeId, buyerUserId);
            if (buyerNotice != null) {
                buyerNotice.setStatus(1);
                cereBuyerNoticeDAO.updateStatus(buyerNotice);
            } else {
                buyerNotice = new CereBuyerNotice();
                buyerNotice.setBuyerUserId(buyerUserId);
                buyerNotice.setNoticeId(noticeId);
                buyerNotice.setStatus(1);
                cereBuyerNoticeDAO.insert(buyerNotice);
            }
        }
    }

    @Override
    public void readNotice(Long noticeId, Long buyerUserId) {
        CereNotice cereNotice = cereNoticeDAO.selectByPrimaryKey(noticeId);
        if (cereNotice == null) {
            return;
        }
        // 公告和站内信，已读 需要 记录到 cere_buyer_notice表
        if (!(IntegerEnum.NOTICE_TYPE_SYSTEM.getCode().equals(cereNotice.getNoticeType()))) {
            CereBuyerNotice buyerNotice = cereBuyerNoticeDAO.selectBuyerNotice(noticeId, buyerUserId);
            if (buyerNotice == null) {
                buyerNotice = new CereBuyerNotice();
                buyerNotice.setNoticeId(noticeId);
                buyerNotice.setBuyerUserId(buyerUserId);
                cereBuyerNoticeDAO.insert(buyerNotice);
            }
        } else {
            // 未读状态
            if (!IntegerEnum.YES.getCode().equals(cereNotice.getIfRead())) {
                cereNotice.setIfRead(IntegerEnum.YES.getCode());
                cereNoticeDAO.updateByPrimaryKeySelective(cereNotice);
            }
        }
    }
}
