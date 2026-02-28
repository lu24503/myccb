/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.after;

import com.shop.cereshop.app.page.after.AfterDetail;
import com.shop.cereshop.app.page.after.AfterHistory;
import com.shop.cereshop.app.page.after.Afters;
import com.shop.cereshop.commons.domain.after.CereOrderAfter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface CereOrderAfterDAO extends BaseMapper<CereOrderAfter> {
    int deleteByPrimaryKey(Long afterId);

    int insertSelective(CereOrderAfter record);

    CereOrderAfter selectByPrimaryKey(Long afterId);

    int updateByPrimaryKeySelective(CereOrderAfter record);

    int updateByPrimaryKey(CereOrderAfter record);

    List<Afters> getAll(@Param("state") Integer state,@Param("buyerUserId") Long buyerUserId);

    AfterDetail getById(@Param("afterId") Long afterId);

    List<AfterHistory> findHistories(@Param("afterId") Long afterId);

    CereOrderAfter checkState(@Param("afterId") Long afterId,@Param("state") Integer state,@Param("afterType") Integer afterType);

    CereOrderAfter checkStateStayAndNo(@Param("afterId") Long afterId);

    CereOrderAfter checkStateStayAndNoAndType(@Param("afterId") Long afterId);

    Integer getPostSaleNumByMonth(@Param("time") Date time, @Param("buyerUserId") Long buyerUserId);

    int findAfterCount(Long buyerUserId);
}
