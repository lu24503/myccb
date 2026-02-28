/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.groupwork;

import com.shop.cereshop.app.page.order.CollagePerson;
import com.shop.cereshop.commons.domain.collage.CereCollageOrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereCollageOrderDetailDAO extends BaseMapper<CereCollageOrderDetail> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(CereCollageOrderDetail record);

    CereCollageOrderDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CereCollageOrderDetail record);

    int updateByPrimaryKey(CereCollageOrderDetail record);

    void updateInvalid(@Param("ids") List<Long> ids);

    CereCollageOrderDetail findKaituan(@Param("collageId") Long collageId);

    List<CollagePerson> findCollagePerson(@Param("collageId") Long collageId);
}
