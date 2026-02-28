/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.compose;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.commons.domain.permission.CerePlatformPermission;
import com.shop.cereshop.commons.domain.tool.CereComposeProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CereComposeProductDAO extends BaseMapper<CerePlatformPermission> {

    int insertSelective(CereComposeProduct record);

    List<Long> selectByComposeId(Long composeId);

    List<CereComposeProduct> selectComposeIdListByProductId(List<Long> productIdList);
}
