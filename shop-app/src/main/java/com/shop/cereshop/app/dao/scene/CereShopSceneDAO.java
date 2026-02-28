/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.scene;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.commons.domain.permission.CerePlatformPermission;
import com.shop.cereshop.commons.domain.scene.CereShopScene;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CereShopSceneDAO extends BaseMapper<CerePlatformPermission> {
    int deleteByPrimaryKey(Long sceneId);

    int insertSelective(CereShopScene record);

    CereShopScene selectByPrimaryKey(Long sceneId);

    int updateByPrimaryKeySelective(CereShopScene record);

    int updateByPrimaryKey(CereShopScene record);

    List<CereShopScene> selectOnGoingFestivalMarketing();

    List<CereShopScene> selectOnGoingHolidayMarketing();

    List<CereShopScene> selectOnGoingBirthDayMarketing();

    List<CereShopScene> selectOnGoingMarketingByShopId(Long shopId);

    List<CereShopScene> selectOnGoingMarketing();
}
