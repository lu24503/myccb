/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.buyer;

import com.shop.cereshop.commons.domain.buyer.CereBuyerReceive;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereBuyerReceiveDAO extends BaseMapper<CereBuyerReceive> {
    int deleteByPrimaryKey(Long receiveId);

    int insertSelective(CereBuyerReceive record);

    CereBuyerReceive selectByPrimaryKey(Long receiveId);

    int updateByPrimaryKeySelective(CereBuyerReceive record);

    int updateByPrimaryKey(CereBuyerReceive record);

    CereBuyerReceive findlatelyReceiveByUserId(@Param("buyerUserId") Long buyerUserId);

    CereBuyerReceive getById(@Param("receiveId") Long receiveId);

    List<CereBuyerReceive> getAll(@Param("buyerUserId") Long buyerUserId);

    CereBuyerReceive findDefaultByUserIdAndSelf(@Param("buyerUserId") Long buyerUserId,@Param("receiveId") Long receiveId);

    CereBuyerReceive findById(@Param("receiveId") Long receiveId);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);
}
