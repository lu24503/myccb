/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.price.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.price.CerePriceProductDAO;
import com.shop.cereshop.app.page.canvas.CanvasProduct;
import com.shop.cereshop.app.page.compose.ComposeProduct;
import com.shop.cereshop.app.param.canvas.CanvasProductParam;
import com.shop.cereshop.app.param.price.ShopPricePageParam;
import com.shop.cereshop.app.service.price.CerePriceProductService;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.tool.CerePriceProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CerePriceProductServiceImpl implements CerePriceProductService {

    @Autowired
    private CerePriceProductDAO cerePriceProductDAO;

    @Override
    public Map<Long, List<Long>> selectProductIdListMap(List<Long> priceIdList, List<Long> productIdList) {
        Map<Long, List<Long>> result = new HashMap<>();
        List<CerePriceProduct> priceProductList = cerePriceProductDAO.selectProductIdList(priceIdList, productIdList);
        Map<Long, List<CerePriceProduct>> map = priceProductList.stream().collect(Collectors.groupingBy(CerePriceProduct::getPriceId));
        for(Map.Entry<Long,List<CerePriceProduct>> entry:map.entrySet()) {
            result.put(entry.getKey(),entry.getValue().stream().map(CerePriceProduct::getProductId).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public Page getPriceProducts(CanvasProductParam param) {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<CanvasProduct> list=cerePriceProductDAO.getPriceProducts(param);
        PageInfo<CanvasProduct> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public Page<ComposeProduct> findProducts(ShopPricePageParam priceParam) {
        PageHelper.startPage(priceParam.getPage(), priceParam.getPageSize());
        List<ComposeProduct> list = cerePriceProductDAO.findProducts(priceParam.getPriceId());
        PageInfo<ComposeProduct> pageInfo=new PageInfo<>(list);
        Page<ComposeProduct> page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }
}
