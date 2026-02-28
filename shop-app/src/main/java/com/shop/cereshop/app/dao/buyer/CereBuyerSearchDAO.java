/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.buyer;

import com.shop.cereshop.commons.domain.buyer.CereBuyerSearch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereBuyerSearchDAO extends BaseMapper<CereBuyerSearch> {
    int deleteByPrimaryKey(Long searchId);

    int insertSelective(CereBuyerSearch record);

    CereBuyerSearch selectByPrimaryKey(Long searchId);

    int updateByPrimaryKeySelective(CereBuyerSearch record);

    int updateByPrimaryKey(CereBuyerSearch record);

    List<CereBuyerSearch> getHistory(@Param("buyerUserId") Long buyerUserId);

    // void delete(@Param("searchId") Long searchId);

    CereBuyerSearch findBySearch(@Param("search") String search,@Param("buyerUserId") Long buyerUserId);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);

    List<String> selectHotSearch();
}
