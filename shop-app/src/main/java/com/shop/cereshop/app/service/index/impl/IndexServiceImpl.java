/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.index.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.product.CereSkuMemberRealInfoDAO;
import com.shop.cereshop.app.page.index.Index;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.index.ProductClassify;
import com.shop.cereshop.app.page.index.RecommendShop;
import com.shop.cereshop.app.param.index.IndexParam;
import com.shop.cereshop.app.param.index.SearchParam;
import com.shop.cereshop.app.service.buyer.CereBuyerSearchService;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.index.IndexService;
import com.shop.cereshop.app.service.product.CereProductClassifyService;
import com.shop.cereshop.app.service.product.CereShopProductService;
import com.shop.cereshop.app.service.shop.CerePlatformShopservice;
import com.shop.cereshop.commons.cache.product.ProductBo;
import com.shop.cereshop.commons.domain.buyer.CereBuyerSearch;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.canvas.CerePlatformCanvas;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private CerePlatformShopservice cerePlatformShopservice;

    @Autowired
    private CereProductClassifyService cereProductClassifyService;

    @Autowired
    private CereBuyerSearchService cereBuyerSearchService;

    @Autowired
    private CereShopProductService cereShopProductService;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    @Autowired
    private CereSkuMemberRealInfoDAO cereSkuMemberRealInfoDAO;

    @Value("${querySkuRealInfo}")
    private boolean querySkuRealInfo;

    @Override
    public Index index(IndexParam param, CereBuyerUser user) throws CoBusinessException {
        Index index = new Index();
        if (user != null) {
            //用户登录过
        } else {
            //第一次进来
        }
        //查询所有一级类目
        List<ProductClassify> classifies = cereProductClassifyService.findAll();
        //查询每周排名前十的好店推荐数据
        List<RecommendShop> shops = cerePlatformShopservice.findRecommendShop();
        if (!EmptyUtils.isEmpty(shops)) {
            shops.stream()
                    //查询该店销量最高商品数据
                    .peek(shop -> shop = cerePlatformShopservice.findVolumeProductByShopId(shop.getShopId()));
        }
        //查询好物推荐数据(销量前10商品数据)
        List<RecommendShop> products = cerePlatformShopservice.findRecommendProducts();
        if (!EmptyUtils.isEmpty(products)) {
            products.forEach(product -> {
                //设置付款人数
                product.setUsers(cerePlatformShopservice.findPayUsers(product.getProductId()));
                //设置活动信息
                ProductBo productBo = cereShopProductService.fetchProductCache(product.getShopId(), product.getProductId(), product.getSkuId(), user, false);
                if (productBo != null) {
                    product.setActivityType(productBo.getActivityType());
                    product.setOriginalPrice(productBo.getOriginalPrice());
                    product.setPrice(productBo.getPrice());
                    product.setTotal(productBo.getTotal());
                }
            });
        }
        index.setClassifies(classifies);
        index.setShops(shops);
        index.setProducts(products);
        return index;
    }

    @Override
    public List<CereBuyerSearch> getHistory(CereBuyerUser user) throws CoBusinessException {
        if (user != null) {
            return cereBuyerSearchService.getHistory(user.getBuyerUserId());
        }
        return null;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public Page getSearchProducts(SearchParam param, CereBuyerUser user) throws CoBusinessException {
        //过滤表情包参数
        param.setSearch(EmptyUtils.filterEmoji(param.getSearch()));
        if (!EmptyUtils.isEmpty(param.getSearch()) && user != null) {
            //查询当前搜索记录是否存在
            CereBuyerSearch cereBuyerSearch = cereBuyerSearchService.findBySearch(param.getSearch(), user.getBuyerUserId());
            if (cereBuyerSearch == null) {
                //如果没有,新增搜索记录
                cereBuyerSearch = new CereBuyerSearch();
                cereBuyerSearch.setBuyerUserId(user.getBuyerUserId());
                cereBuyerSearch.setSearch(param.getSearch());
                cereBuyerSearchService.insert(cereBuyerSearch);
            }
        }
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<Product> list = new ArrayList<>();
        if (querySkuRealInfo) {
            if (user != null && user.getMemberLevelId() > 0) {
                param.setMemberLevelId(user.getMemberLevelId());
            }
            list = cereSkuMemberRealInfoDAO.getSearchProducts(param);
        } else {
            list = cereShopProductService.getSearchProducts(param);
            if (!EmptyUtils.isEmpty(list)) {
                list.forEach(a -> {
                    //查询付款人数
                    a.setUsers(cerePlatformShopservice.findPayUsers(a.getProductId()));
                    //设置活动相关信息
                    ProductBo productBo = cereShopProductService.fetchProductCache(a.getShopId(), a.getProductId(), a.getSkuId(), user, false);
                    if (productBo != null) {
                        a.setActivityType(productBo.getActivityType());
                        a.setOriginalPrice(productBo.getOriginalPrice());
                        a.setPrice(productBo.getPrice());
                    }
                });
                if (param.getType() != null) {
                    if (param.getType() == 1) {
                        list = list.stream().sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
                    } else if (param.getType() == 2) {
                        list = list.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).collect(Collectors.toList());
                    }
                }
            }
        }

        PageInfo<Product> pageInfo = new PageInfo<>(list);
        Page page = new Page(pageInfo.getList(), pageInfo.getTotal());
        return page;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public void deleteSearch(SearchParam param) throws CoBusinessException {
        cereBuyerSearchService.delete(param.getSearchId());
    }

    @Override
    public CerePlatformCanvas getCanvas(CerePlatformCanvas canvas) throws CoBusinessException {
        return cereBuyerUserService.getCanvas(canvas);
    }
}
