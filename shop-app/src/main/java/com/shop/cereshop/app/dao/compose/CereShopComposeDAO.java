/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.compose;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.param.compose.CereShopComposeDTO;
import com.shop.cereshop.commons.domain.permission.CerePlatformPermission;
import com.shop.cereshop.commons.domain.tool.CereShopCompose;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CereShopComposeDAO extends BaseMapper<CerePlatformPermission> {
    int deleteByPrimaryKey(Long composeId);

    int insertSelective(CereShopCompose record);

    CereShopCompose selectByPrimaryKey(Long composeId);

    int updateByPrimaryKeySelective(CereShopCompose record);

    int updateByPrimaryKey(CereShopCompose record);

    CereShopCompose selectOnGoingByComposeId(Long composeId);

    List<Long> selectComposeIdListContainsProductId(Long productId);

    List<CereShopCompose> selectByComposeIdList(List<Long> composeIdList);

    List<CereShopComposeDTO> selectByShopIdList(List<Long> shopIdList);
}
