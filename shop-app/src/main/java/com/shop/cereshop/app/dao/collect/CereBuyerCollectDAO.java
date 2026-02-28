/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.collect;

import com.shop.cereshop.app.page.collect.CollectProduct;
import com.shop.cereshop.app.page.collect.CollectShop;
import com.shop.cereshop.commons.domain.collect.CereBuyerCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereBuyerCollectDAO extends BaseMapper<CereBuyerCollect> {
    int deleteByPrimaryKey(Long collectId);

    int insertSelective(CereBuyerCollect record);

    CereBuyerCollect selectByPrimaryKey(Long collectId);

    int updateByPrimaryKeySelective(CereBuyerCollect record);

    int updateByPrimaryKey(CereBuyerCollect record);

    CereBuyerCollect findByUserProduct(@Param("buyerUserId") Long buyerUserId,@Param("productId") Long productId);

    CereBuyerCollect findByUserShopId(@Param("buyerUserId") Long buyerUserId,@Param("shopId") Long shopId);

    void cancel(@Param("ids") List<Long> ids,@Param("buyerUserId") Long buyerUserId,@Param("time") String time);

    void newCancel(@Param("shopId") Long shopId,@Param("buyerUserId") Long buyerUserId,@Param("time") String time);

    List<CollectProduct> getAllProduct(@Param("buyerUserId") Long buyerUserId,@Param("search") String search);

    List<CollectShop> getAllShop(@Param("buyerUserId") Long buyerUserId,@Param("search") String search);

    void updateSelected(@Param("ids") List<Long> ids);

    void deleteByIds(@Param("ids") List<Long> ids);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);

    int findShopCollectCount(Long shopId);
}
