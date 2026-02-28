/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.notice;

import com.shop.cereshop.app.page.notice.BuyerNotice;
import com.shop.cereshop.app.page.notice.NoticeDetail;
import com.shop.cereshop.commons.domain.notice.CereNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereNoticeDAO extends BaseMapper<CereNotice> {
    int deleteByPrimaryKey(Long noticeId);

    int insertSelective(CereNotice record);

    CereNotice selectByPrimaryKey(Long noticeId);

    int updateByPrimaryKeySelective(CereNotice record);

    int updateByPrimaryKey(CereNotice record);

    List<BuyerNotice> getAll(@Param("buyerUserId") Long buyerUserId,@Param("createTime") String createTime);

    NoticeDetail getById(@Param("noticeId") Long noticeId);

    List<BuyerNotice> getGongGaoAll(@Param("buyerUserId") Long buyerUserId);

    void insertBatch(@Param("list") List<CereNotice> list);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);

    int getNotRead(@Param("buyerUserId") Long buyerUserId,@Param("createTime")String createTime);

    @Deprecated
    int getRead(@Param("buyerUserId") Long buyerUserId);

    List<Long> findIdsByOrderId(@Param("orderId") Long orderId);

    void deleteByIds(@Param("ids") List<Long> ids);

    void deleteBuyerNotice(@Param("ids") List<Long> ids,@Param("buyerUserId") Long buyerUserId);

    List<BuyerNotice> getNotices();

    int readAll(Long buyerUserId);

    List<BuyerNotice> selectNotReadMessage(Long buyerUserId);
}
