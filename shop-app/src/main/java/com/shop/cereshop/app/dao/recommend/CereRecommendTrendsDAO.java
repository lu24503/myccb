/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.recommend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.recommend.BusinessShopInfo;
import com.shop.cereshop.app.page.recommend.CereRecommendTrendDetailVO;
import com.shop.cereshop.app.page.recommend.CereRecommendTrendPageVO;
import com.shop.cereshop.app.param.recommend.RecommendTrendPageParam;
import com.shop.cereshop.commons.domain.recommend.CereRecommendTrends;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereRecommendTrendsDAO extends BaseMapper<CereRecommendTrends> {
    List<CereRecommendTrendPageVO> trendPage(RecommendTrendPageParam param);

    CereRecommendTrendDetailVO getTrendById(@Param("recommendId") Long recommendId);

    BusinessShopInfo getPlatformShop(@Param("shopId") Long shopId, @Param("userId") Long userId);

    void addCommentCount(@Param("recommendId") Long recommendId);

    void subCommentCount(@Param("recommendId") Long recommendId, @Param("count") Integer count);

    void addLikesCount(@Param("recommendId") Long recommendId);

    void subLikesCount(@Param("recommendId") Long recommendId);

    void addShareCount(@Param("recommendId") Long recommendId);
}
