package com.shop.cereshop.app.service.recommend;


import com.shop.cereshop.app.page.recommend.BusinessShopInfo;
import com.shop.cereshop.app.page.recommend.CereRecommendTrendDetailVO;
import com.shop.cereshop.app.page.recommend.CereRecommendTrendPageVO;
import com.shop.cereshop.app.param.recommend.RecommendLikesParam;
import com.shop.cereshop.app.param.recommend.RecommendTrendPageParam;
import com.shop.cereshop.commons.domain.common.Page;

public interface CereRecommendTrendsService {

    Page<CereRecommendTrendPageVO> trendPage(RecommendTrendPageParam param);

    CereRecommendTrendDetailVO getTrend(RecommendLikesParam param) throws Exception;

    void share(Long recommendId);

    BusinessShopInfo getShopInfo(Long shopId, Long userId);
}
