/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.scene.impl;

import com.shop.cereshop.app.dao.scene.CereShopSceneMemberCouponDAO;
import com.shop.cereshop.app.service.scene.CereShopSceneMemberCouponService;
import com.shop.cereshop.commons.domain.scene.CereShopSceneMemberCoupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereShopSceneMemberCouponServiceImpl implements CereShopSceneMemberCouponService {

    @Autowired
    private CereShopSceneMemberCouponDAO cereShopSceneMemberCouponDAO;

    @Override
    public List<CereShopSceneMemberCoupon> selectBySceneId(Long sceneId) {
        return cereShopSceneMemberCouponDAO.selectBySceneId(sceneId);
    }
}
