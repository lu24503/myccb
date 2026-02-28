/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.platformtool;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.commons.domain.platformtool.CereBuyerPoliteRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereBuyerPoliteRecordDAO extends BaseMapper<CereBuyerPoliteRecord> {

    int insertSelective(CereBuyerPoliteRecord record);

    List<CereBuyerPoliteRecord> selectPoliteRecord(@Param("buyerUserId")Long buyerUserId,
                                                   @Param("orderId")Long orderId);
}
