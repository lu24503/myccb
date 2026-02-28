/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.recommend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.param.recommend.RecommendLikesParam;
import com.shop.cereshop.commons.domain.recommend.CereRecommendLikes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CereRecommendLikesDAO extends BaseMapper<CereRecommendLikes> {

    void saveLikes(CereRecommendLikes likes);

    void deleteLikes(RecommendLikesParam param);

    CereRecommendLikes getLikes(RecommendLikesParam param);
}
