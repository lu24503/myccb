/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.notice;

import com.shop.cereshop.commons.domain.notice.CereBuyerNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CereBuyerNoticeDAO extends BaseMapper<CereBuyerNotice> {

    int insertSelective(CereBuyerNotice record);

    CereBuyerNotice selectBuyerNotice(@Param("noticeId") Long noticeId, @Param("buyerUserId") Long buyerUserId);

    int updateStatus(CereBuyerNotice buyerNotice);
}
