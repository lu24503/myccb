/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.groupwork.impl;

import com.shop.cereshop.app.dao.groupwork.CereShopGroupWorkDetailDAO;
import com.shop.cereshop.app.domain.activity.ActivityData;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.service.groupwork.CereShopGroupWorkDetailService;
import com.shop.cereshop.commons.domain.tool.CereShopGroupWorkDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CereShopGroupWorkDetailServiceImpl implements CereShopGroupWorkDetailService {

    @Autowired
    private CereShopGroupWorkDetailDAO cereShopGroupWorkDetailDAO;

    @Override
    public BigDecimal findPriceByGroupWorkIdAndSkuId(Long shopGroupWorkId, Long skuId) {
        return cereShopGroupWorkDetailDAO.findPriceByGroupWorkIdAndSkuId(shopGroupWorkId, skuId);
    }

    @Override
    public ActivityData findPriceBySkuId(Long skuId) {
        return cereShopGroupWorkDetailDAO.findPriceBySkuId(skuId);
    }

    @Override
    public List<ToolProduct> findDistinctProducts(Long shopGroupWorkId) {
        return cereShopGroupWorkDetailDAO.findDistinctProducts(shopGroupWorkId);
    }

    @Override
    public Long findSkuIdByProductId(Long productId) {
        return cereShopGroupWorkDetailDAO.findSkuIdByProductId(productId);
    }
}
