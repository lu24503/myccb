/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.notice;

import com.shop.cereshop.app.page.notice.BuyerNotice;
import com.shop.cereshop.app.page.notice.NoticeDetail;
import com.shop.cereshop.app.param.notice.NoticeIdParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.notice.CereNotice;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereNoticeService {
    Page getAll(PageParam pageParam,CereBuyerUser user) throws CoBusinessException;

    NoticeDetail getById(NoticeIdParam param) throws CoBusinessException;

    List<BuyerNotice> getGongGaoAll(CereBuyerUser user) throws CoBusinessException;

    void insert(CereNotice cereNotice) throws CoBusinessException;

    void insertBatch(List<CereNotice> notices) throws CoBusinessException;

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;

    int getNotRead(Long buyerUserId,String createTime);

    void update(CereNotice cereNotice) throws CoBusinessException;

    @Deprecated
    int getRead(Long buyerUserId);

    List<Long> findIdsByOrderId(Long orderId);

    void deleteByIds(List<Long> noticeIds) throws CoBusinessException;

    void deleteBuyerNotice(List<Long> noticeIds, Long buyerUserId) throws CoBusinessException;

    List<BuyerNotice> getNotices();

    void readAll(Long buyerUserId);

    void removeById(Long noticeId, Long buyerUserId);

    void readNotice(Long noticeId, Long buyerUserId);
}
