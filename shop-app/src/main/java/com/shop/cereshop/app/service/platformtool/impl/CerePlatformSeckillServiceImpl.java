/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.platformtool.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.platformtool.CereBuyerPlatformSeckillVisitDAO;
import com.shop.cereshop.app.dao.platformtool.CerePlatformSeckillDAO;
import com.shop.cereshop.app.page.canvas.CanvasPlatformSeckill;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.seckill.PlatformSeckillProduct;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.app.param.seckill.PlatformSeckillParam;
import com.shop.cereshop.app.param.seckill.ShopPlatformSeckill;
import com.shop.cereshop.app.service.platformtool.CerePlatformSeckillService;
import com.shop.cereshop.app.service.product.CereProductSkuService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerPlatformSeckillVisit;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformSeckill;
import com.shop.cereshop.commons.domain.product.Sku;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class CerePlatformSeckillServiceImpl implements CerePlatformSeckillService {

    @Autowired
    private CerePlatformSeckillDAO cerePlatformSeckillDAO;

    @Autowired
    private CereBuyerPlatformSeckillVisitDAO cereBuyerPlatformSeckillVisitDAO;

    @Autowired
    private CereProductSkuService cereProductSkuService;

    @Override
    public List<String> querySession() {
        return cerePlatformSeckillDAO.queryTodaySession();
    }

    @Override
    public Page<PlatformSeckillProduct> queryProductListBySession(PlatformSeckillParam platformSeckillParam) {
        PageHelper.startPage(platformSeckillParam.getPage(), platformSeckillParam.getPageSize());
        String session = platformSeckillParam.getSession();
        String lastHour = session;
        try {
            lastHour = TimeUtils.getMoreHourAfter(session + ":00", 1).substring(0, 16);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        platformSeckillParam.setNextHour(lastHour);
        List<PlatformSeckillProduct> list = cerePlatformSeckillDAO.queryProductListBySession(platformSeckillParam);
        PageInfo<PlatformSeckillProduct> pageInfo=new PageInfo<>(list);
        return new Page(pageInfo.getList(),pageInfo.getTotal());
    }

    @Override
    public List<CanvasPlatformSeckill> getPlatformSeckills(RenovationParam param) {
        if (CollectionUtils.isEmpty(param.getStateList())) {
            param.setStateList(Arrays.asList(2L, 3L));
        }
        List<CanvasPlatformSeckill> list = cerePlatformSeckillDAO.getPlatformSeckills(param);
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(seckill -> {
                //查询商品明细
                seckill.setProducts(cerePlatformSeckillDAO.findSeckillProduct(seckill.getSeckillId()));
            });
        }
        return list;
    }

    @Override
    public CerePlatformSeckill findById(Long platformSeckillId) {
        return cerePlatformSeckillDAO.selectByPrimaryKey(platformSeckillId);
    }

    @Override
    public void insertVisit(CereBuyerPlatformSeckillVisit cereBuyerPlatformSeckillVisit) {
        cereBuyerPlatformSeckillVisitDAO.insert(cereBuyerPlatformSeckillVisit);
    }

    @Override
    public List<Long> queryProductIdListBySeckillId(Long seckillId) {
        return cerePlatformSeckillDAO.queryProductIdListBySeckillId(seckillId);
    }

    @Override
    public List<ShopPlatformSeckill> selectPlatformSeckillsByShopIdList(List<Long> shopIdList) {
        return cerePlatformSeckillDAO.selectByShopIdList(shopIdList);
    }

    @Override
    public ProductDetail setActivityInfo(Long platformSeckillId, CereBuyerUser user, ProductDetail detail) {
        try {
            CerePlatformSeckill platformSeckill = cerePlatformSeckillDAO.selectByPrimaryKey(platformSeckillId);
            if (platformSeckill != null) {
                String time = TimeUtils.yyMMddHHmmss();
                detail.setIfAdd(platformSeckill.getIfAdd());
                detail.setStartTime(platformSeckill.getStartTime());
                detail.setEndTime(platformSeckill.getEndTime());
                BigDecimal price = detail.getOriginalPrice();
                if (IntegerEnum.ACTIVITY_START.getCode().equals(platformSeckill.getState())) {
                    detail.setTime(TimeUtils.getCountDownByTime(time,platformSeckill.getEndTime()));
                    detail.setIfEnable(IntegerEnum.NO.getCode());
                    price = price.subtract(platformSeckill.getSeckillMoney());
                } else if (TimeUtils.parseDate(platformSeckill.getSignEndTime(), "yyyy-MM-dd HH:mm:ss").before(new Date())) {
                    //当前时间已经是报名结束之间之后
                    detail.setIfEnable(IntegerEnum.YES.getCode());
                    price = price.subtract(platformSeckill.getSeckillMoney());
                }
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    price = BigDecimal.ZERO;
                }
                detail.setPlatformSeckillId(platformSeckillId);
                if(user!=null){
                    //新增浏览记录
                    CereBuyerPlatformSeckillVisit cereBuyerPlatformSeckillVisit=new CereBuyerPlatformSeckillVisit();
                    cereBuyerPlatformSeckillVisit.setBuyerUserId(user.getBuyerUserId());
                    cereBuyerPlatformSeckillVisit.setSeckillId(platformSeckillId);
                    cereBuyerPlatformSeckillVisit.setVisitTime(TimeUtils.yyMMddHHmmss());
                    cereBuyerPlatformSeckillVisit.setShopId(detail.getShopId());
                    cereBuyerPlatformSeckillVisitDAO.insert(cereBuyerPlatformSeckillVisit);
                }

                //设置库存总数 剩余库存数 原始价格 当前价格
                ProductStockInfo stockInfo = cerePlatformSeckillDAO.selectProductStockInfo(platformSeckillId, detail.getProductId());
                if (stockInfo != null) {
                    detail.setTotal(stockInfo.getTotal());
                    detail.setSurplusNumber(stockInfo.getStockNumber());
                }
                //detail.setOriginalPrice(detail.getPrice());
                detail.setPrice(price);

                //查询该商品所有组合规格数据封装到map
                List<Sku> skus=cereProductSkuService.findSimpleSkuByProductId(detail.getProductId());
                if(!EmptyUtils.isEmpty(skus)) {
                    Map<String, Sku> map = new HashMap<>();
                    String image=detail.getImage();
                    skus.forEach(sku -> {
                        //由于平台秒杀是商品级别，所以全部sku的活动数据都和商品同步
                        sku.setActivityType(detail.getActivityType());
                        sku.setPlatformSeckillId(platformSeckillId);
                        sku.setStartTime(detail.getStartTime());
                        sku.setEndTime(detail.getEndTime());
                        sku.setTime(detail.getTime());
                        sku.setIfEnable(detail.getIfEnable());
                        sku.setTotal(detail.getTotal());
                        sku.setStockNumber(detail.getSurplusNumber());

                        if (IntegerEnum.ACTIVITY_START.getCode().equals(platformSeckill.getState())
                            || TimeUtils.parseDate(platformSeckill.getSignEndTime(), "yyyy-MM-dd HH:mm:ss").before(new Date())) {
                            sku.setPrice(sku.getOriginalPrice().subtract(platformSeckill.getSeckillMoney()));
                            if (sku.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                                sku.setPrice(BigDecimal.ZERO);
                            }
                        }
                        if (EmptyUtils.isEmpty(sku.getImage())) {
                            sku.setImage(image);
                        }
                        if (EmptyUtils.isEmpty(sku.getValueCodes())) {
                            sku.setValueCodes("单款项");
                        }
                        map.put(sku.getValueCodes(), sku);
                    });
                    detail.setMap(map);
                }
            }
        } catch (Exception e) {
            log.error("setActivtyInfo platformSeckillId = {}, productId = {}, errorMessage = {}", platformSeckillId, detail.getProductId(), e.getMessage(), e);
        }
        return detail;
    }

    @Override
    public List<CerePlatformSeckill> queryPlatformSeckillList() {
        return cerePlatformSeckillDAO.queryPlatformSeckillList();
    }

    @Override
    public ProductDetail setActivityInfoForRealInfo(Long platformSeckillId, CereBuyerUser user, ProductDetail detail) {
        try {
            if (user != null) {
                //新增浏览记录
                CereBuyerPlatformSeckillVisit cereBuyerPlatformSeckillVisit=new CereBuyerPlatformSeckillVisit();
                cereBuyerPlatformSeckillVisit.setBuyerUserId(user.getBuyerUserId());
                cereBuyerPlatformSeckillVisit.setSeckillId(platformSeckillId);
                cereBuyerPlatformSeckillVisit.setVisitTime(TimeUtils.yyMMddHHmmss());
                cereBuyerPlatformSeckillVisit.setShopId(detail.getShopId());
                cereBuyerPlatformSeckillVisitDAO.insert(cereBuyerPlatformSeckillVisit);
            }

            //设置库存总数 剩余库存数 原始价格 当前价格
            ProductStockInfo stockInfo = cerePlatformSeckillDAO.selectProductStockInfo(platformSeckillId, detail.getProductId());
            if (stockInfo != null) {
                detail.setTotal(stockInfo.getTotal());
                detail.setSurplusNumber(stockInfo.getStockNumber());
            }
        } catch (Exception e) {
            log.error("setActivtyInfo platformSeckillId = {}, productId = {}, errorMessage = {}", platformSeckillId, detail.getProductId(), e.getMessage(), e);
        }
        return detail;
    }

}
