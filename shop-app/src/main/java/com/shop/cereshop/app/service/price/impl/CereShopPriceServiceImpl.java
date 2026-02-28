/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.price.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.price.CereShopPriceDAO;
import com.shop.cereshop.app.page.compose.ComposeProduct;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.price.ShopPriceDetail;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.app.param.price.CereShopPriceDTO;
import com.shop.cereshop.app.param.price.ShopPricePageParam;
import com.shop.cereshop.app.param.price.ShopPriceWithRule;
import com.shop.cereshop.app.service.price.CerePriceProductService;
import com.shop.cereshop.app.service.price.CerePriceRuleService;
import com.shop.cereshop.app.service.price.CereShopPriceService;
import com.shop.cereshop.app.service.shop.CerePlatformShopservice;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.tool.CerePriceRule;
import com.shop.cereshop.commons.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CereShopPriceServiceImpl implements CereShopPriceService {

    @Autowired
    private CereShopPriceDAO cereShopPriceDAO;

    @Autowired
    private CerePlatformShopservice cerePlatformShopservice;

    @Autowired
    private CerePriceProductService cerePriceProductService;

    @Autowired
    private CerePriceRuleService cerePriceRuleService;

    @Override
    public Map<Long, Map<Long, List<CerePriceRule>>> selectPriceMap(List<Long> shopIdList) {
        List<ShopPriceWithRule> list = cereShopPriceDAO.selectPriceList(shopIdList);
        Map<Long, List<ShopPriceWithRule>> map = list.stream().collect(Collectors.groupingBy(ShopPriceWithRule::getShopId));
        Map<Long, Map<Long, List<CerePriceRule>>> result = new HashMap<>();
        for(Map.Entry<Long, List<ShopPriceWithRule>> entry:map.entrySet()) {
            Long shopId = entry.getKey();
            Map<Long, List<CerePriceRule>> priceMap = entry.getValue().stream().map(obj -> {
                CerePriceRule priceRule = new CerePriceRule();
                priceRule.setPriceId(obj.getPriceId());
                priceRule.setNumber(obj.getNumber());
                priceRule.setPrice(obj.getPrice());
                return priceRule;
            }).collect(Collectors.groupingBy(CerePriceRule::getPriceId));
            result.put(shopId, priceMap);
        }
        return result;
    }

    @Override
    public Page<Product> selectProductListByPriceId(ShopPricePageParam param) {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<Product> productList = cereShopPriceDAO.selectProductListByPriceId(param);
        PageInfo<Product> pageInfo = new PageInfo<>(productList);
        if(!EmptyUtils.isEmpty(productList)){
            productList.forEach(a -> {
                //查询付款人数
                a.setUsers(cerePlatformShopservice.findPayUsers(a.getProductId()));
            });
        }
        return new Page<>(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    public List<ShopPriceDetail> getPrices(RenovationParam param) {
        List<ShopPriceDetail> list=cereShopPriceDAO.getPrices(param);
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(detail -> {
                //查询定价商品数据
                PageHelper.startPage(param.getPage(),param.getPageSize());
                ShopPricePageParam priceParam = new ShopPricePageParam();
                priceParam.setPriceId(detail.getPriceId());
                priceParam.setPage(param.getPage());
                priceParam.setPageSize(param.getPageSize());
                Page<ComposeProduct> page = cerePriceProductService.findProducts(priceParam);
                detail.setComposeProducts(page.getList());
                detail.setTotal(page.getTotal());
                //查询定价规则数据
                detail.setRules(cerePriceRuleService.findRules(detail.getPriceId()));
            });
        }
        return list;
    }

    @Override
    public List<CereShopPriceDTO> selectByShopIdList(List<Long> shopIdList) {
        return null;
    }

    @Override
    public Long selectPriceByProductId(Long productId) {
        return cereShopPriceDAO.selectPriceByProductId(productId);
    }
}
