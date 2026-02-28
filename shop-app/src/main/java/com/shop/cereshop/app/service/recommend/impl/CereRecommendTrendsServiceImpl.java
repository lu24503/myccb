package com.shop.cereshop.app.service.recommend.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.collect.CereBuyerCollectDAO;
import com.shop.cereshop.app.dao.recommend.CereRecommendLikesDAO;
import com.shop.cereshop.app.dao.recommend.CereRecommendRelationDAO;
import com.shop.cereshop.app.dao.recommend.CereRecommendTrendsDAO;
import com.shop.cereshop.app.page.collect.CollectShop;
import com.shop.cereshop.app.page.recommend.BusinessShopInfo;
import com.shop.cereshop.app.page.recommend.CereRecommendTrendDetailVO;
import com.shop.cereshop.app.page.recommend.CereRecommendTrendPageVO;
import com.shop.cereshop.app.param.recommend.RecommendLikesParam;
import com.shop.cereshop.app.param.recommend.RecommendTrendPageParam;
import com.shop.cereshop.app.service.recommend.CereRecommendTrendsService;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CereRecommendTrendsServiceImpl implements CereRecommendTrendsService {

    @Autowired
    private CereRecommendTrendsDAO cereRecommendTrendsDAO;

    @Autowired
    private CereRecommendRelationDAO cereRecommendRelationDAO;

    @Autowired
    private CereBuyerCollectDAO cereBuyerCollectDAO;

    @Autowired
    private CereRecommendLikesDAO cereRecommendLikesDAO;

    @Override
    public Page<CereRecommendTrendPageVO> trendPage(RecommendTrendPageParam param) {
        // 关注店铺
        if (param.getRecommendType() != null && param.getRecommendType() == 0) {
            List<CollectShop> collectShops = cereBuyerCollectDAO.getAllShop(param.getUserId(), null);
            if (CollectionUtil.isNotEmpty(collectShops)) {
                param.setCollectShopIds(collectShops.stream().map(CollectShop::getShopId).collect(Collectors.toList()));
            } else {
                param.setShopId(0l);
            }
        }
        PageHelper.startPage(param.getPage(), param.getPageSize());
        PageInfo<CereRecommendTrendPageVO> pageInfo = new PageInfo<>(cereRecommendTrendsDAO.trendPage(param));
        Page<CereRecommendTrendPageVO> page = new Page(pageInfo.getList(), pageInfo.getTotal());
        return page;
    }

    @Override
    public CereRecommendTrendDetailVO getTrend(RecommendLikesParam param) throws Exception {
        CereRecommendTrendDetailVO vo = cereRecommendTrendsDAO.getTrendById(param.getRecommendId());
        if (ObjectUtil.isNotEmpty(vo.getPublishTime())) {
            int day = TimeUtils.differentDaysByMillisecond(vo.getPublishTime());
            vo.setDay(day > 0 ? day + "天前" : "今天");
        }
        BusinessShopInfo shopInfo = cereRecommendTrendsDAO.getPlatformShop(vo.getShopId(), param.getUserId());
        vo.setProducts(cereRecommendRelationDAO.getRecommendProducts(param.getRecommendId()));
        vo.setLikeStatus(ObjectUtil.isNotNull(cereRecommendLikesDAO.getLikes(param)) ? 1 : 0);
        vo.setIsCollect(shopInfo.getIsCollect());
        vo.setAvatar(shopInfo.getAvatar());
        vo.setName(shopInfo.getName());
        return vo;
    }

    @Override
    public void share(Long recommendId) {
        cereRecommendTrendsDAO.addShareCount(recommendId);
    }

    @Override
    public BusinessShopInfo getShopInfo(Long shopId, Long userId) {
        return cereRecommendTrendsDAO.getPlatformShop(shopId, userId);
    }
}
