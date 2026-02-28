/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.activity.impl;

import com.shop.cereshop.app.dao.activity.CerePlatformActivityDetailDAO;
import com.shop.cereshop.app.service.activity.CerePlatformActivityDetailService;
import com.shop.cereshop.commons.domain.activity.CereBuyerCoupon;
import com.shop.cereshop.commons.domain.activity.CerePlatformActivityDetail;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CerePlatformActivityDetailServiceImpl implements CerePlatformActivityDetailService {

    @Autowired
    private CerePlatformActivityDetailDAO cerePlatformActivityDetailDAO;

    @Override
    public CereBuyerCoupon findByCouponId(Long couponId) {
        return cerePlatformActivityDetailDAO.findByCouponId(couponId);
    }
}
