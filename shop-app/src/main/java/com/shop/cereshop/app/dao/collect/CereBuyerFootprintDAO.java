/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.collect;

import com.shop.cereshop.app.page.collect.BuyerFootprint;
import com.shop.cereshop.app.page.collect.FootprintProduct;
import com.shop.cereshop.commons.domain.collect.CereBuyerFootprint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereBuyerFootprintDAO extends BaseMapper<CereBuyerFootprint> {
    int deleteByPrimaryKey(Long footprintId);

    int insertSelective(CereBuyerFootprint record);

    CereBuyerFootprint selectByPrimaryKey(Long footprintId);

    int updateByPrimaryKeySelective(CereBuyerFootprint record);

    int updateByPrimaryKey(CereBuyerFootprint record);

    void deleteByIds(@Param("ids") List<Long> ids,@Param("buyerUserId") Long buyerUserId,@Param("times") List<String> times);

    List<BuyerFootprint> getAll(@Param("buyerUserId") Long buyerUserId);

    List<FootprintProduct> findProducts(@Param("createTime") String createTime,@Param("buyerUserId") Long buyerUserId);

    void updateSelected(@Param("ids") List<Long> ids);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);
}
