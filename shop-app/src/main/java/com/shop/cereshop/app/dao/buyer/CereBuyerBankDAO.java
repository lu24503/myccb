/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.buyer;

import com.shop.cereshop.commons.domain.buyer.CereBuyerBank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereBuyerBankDAO extends BaseMapper<CereBuyerBank> {
    int insertSelective(CereBuyerBank record);

    CereBuyerBank getById(@Param("bankId") Long bankId);

    List<CereBuyerBank> getAll(@Param("buyerUserId") Long buyerUserId);

    CereBuyerBank checkBank(@Param("buyerUserId") Long buyerUserId,@Param("bankCard") String bankCard,@Param("bankId") Long bankId);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);
}
