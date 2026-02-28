/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.buyer;

import com.shop.cereshop.commons.domain.buyer.CereBuyerWithdrawal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CereBuyerWithdrawalDAO extends BaseMapper<CereBuyerWithdrawal> {
    int deleteByPrimaryKey(Long withdrawalId);

    int insertSelective(CereBuyerWithdrawal record);

    CereBuyerWithdrawal selectByPrimaryKey(Long withdrawalId);

    int updateByPrimaryKeySelective(CereBuyerWithdrawal record);

    int updateByPrimaryKey(CereBuyerWithdrawal record);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId, @Param("id") Long id);
}
