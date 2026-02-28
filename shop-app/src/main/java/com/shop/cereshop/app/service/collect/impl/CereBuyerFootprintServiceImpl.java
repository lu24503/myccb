/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.collect.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.collect.CereBuyerFootprintDAO;
import com.shop.cereshop.app.page.collect.BuyerFootprint;
import com.shop.cereshop.app.page.collect.FootprintProduct;
import com.shop.cereshop.app.param.collect.FootprintIdParam;
import com.shop.cereshop.app.service.collect.CereBuyerFootprintService;
import com.shop.cereshop.app.service.product.CereShopProductService;
import com.shop.cereshop.commons.cache.product.ProductBo;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.collect.CereBuyerFootprint;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CereBuyerFootprintServiceImpl implements CereBuyerFootprintService {

    @Autowired
    private CereBuyerFootprintDAO cereBuyerFootprintDAO;

    @Autowired
    private CereShopProductService cereShopProductService;

    @Override
    public void insert(CereBuyerFootprint cereBuyerFootprint) throws CoBusinessException {
        cereBuyerFootprintDAO.insert(cereBuyerFootprint);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void delete(FootprintIdParam param, CereBuyerUser user) throws CoBusinessException {
        //批量删除足迹
        cereBuyerFootprintDAO.deleteByIds(param.getIds(),user.getBuyerUserId(),param.getTimes());
    }

    @Override
    public Page getAll(PageParam param,Long buyerUserId) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        //查询当前客户所有足迹根据浏览时间分组
        List<BuyerFootprint> list=cereBuyerFootprintDAO.getAll(buyerUserId);
        if(!EmptyUtils.isEmpty(list)){
            //遍历时间,查询所有商品数据
            list.forEach(footprint -> {
                List<FootprintProduct> productList = cereBuyerFootprintDAO.findProducts(footprint.getCreateTime(),buyerUserId);
                productList.forEach(product-> {
                    ProductBo productBo = cereShopProductService.fetchProductCache(product.getShopId(), product.getProductId(), product.getSkuId(), null, false);
                    if (productBo != null) {
                        product.setActivityType(productBo.getActivityType());
                        product.setOriginalPrice(productBo.getOriginalPrice());
                        product.setPrice(productBo.getPrice());
                    }
                });
                footprint.setProducts(productList);
            });
        }
        PageInfo<BuyerFootprint> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void selected(FootprintIdParam param, CereBuyerUser user) throws CoBusinessException {
        //选中足迹
        cereBuyerFootprintDAO.updateSelected(param.getIds());
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereBuyerFootprintDAO.updateBuyerData(buyerUserId,id);
    }

    @Override
    public int findFootprintCount(Long buyerUserId) {
        return cereBuyerFootprintDAO.selectCount(Wrappers.<CereBuyerFootprint>lambdaQuery().eq(CereBuyerFootprint::getBuyerUserId, buyerUserId));
    }
}
