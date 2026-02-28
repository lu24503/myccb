package com.shop.cereshop.app.service.recommend.impl;

import com.shop.cereshop.app.dao.recommend.CereRecommendLikesDAO;
import com.shop.cereshop.app.dao.recommend.CereRecommendTrendsDAO;
import com.shop.cereshop.app.param.recommend.RecommendLikesParam;
import com.shop.cereshop.app.service.recommend.CereRecommendLikesService;
import com.shop.cereshop.commons.domain.recommend.CereRecommendLikes;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CereRecommendLikesServiceImpl implements CereRecommendLikesService {

    @Autowired
    private CereRecommendLikesDAO cereRecommendLikesDAO;

    @Autowired
    private CereRecommendTrendsDAO cereRecommendTrendsDAO;

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void like(RecommendLikesParam param) {
        if(param.getType() == 1){
            CereRecommendLikes likes = new CereRecommendLikes();
            likes.setRecommendId(param.getRecommendId());
            likes.setUserId(param.getUserId());
            cereRecommendLikesDAO.saveLikes(likes);
            cereRecommendTrendsDAO.addLikesCount(param.getRecommendId());
        }else {
            cereRecommendLikesDAO.deleteLikes(param);
            cereRecommendTrendsDAO.subLikesCount(param.getRecommendId());
        }
    }
}
