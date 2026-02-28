/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.recommend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.recommend.CereRecommendTypeVO;
import com.shop.cereshop.commons.domain.recommend.CereRecommendType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CereRecommendTypeDAO extends BaseMapper<CereRecommendType> {
    List<CereRecommendTypeVO> getAll();
}
