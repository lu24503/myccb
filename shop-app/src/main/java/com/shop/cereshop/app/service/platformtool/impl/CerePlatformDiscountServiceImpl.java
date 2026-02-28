/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.platformtool.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.platformtool.CereBuyerPlatformDiscountVisitDAO;
import com.shop.cereshop.app.dao.platformtool.CerePlatformDiscountDAO;
import com.shop.cereshop.app.page.canvas.CanvasPlatformDiscount;
import com.shop.cereshop.app.page.discount.PlatformDiscountProduct;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.commons.domain.product.Sku;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.app.param.discount.PlatformDiscountParam;
import com.shop.cereshop.app.param.discount.ShopPlatformDiscount;
import com.shop.cereshop.app.service.platformtool.CerePlatformDiscountService;
import com.shop.cereshop.app.service.product.CereProductSkuService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerPlatformDiscountVisit;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformDiscount;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@Service
public class CerePlatformDiscountServiceImpl implements CerePlatformDiscountService {

    @Autowired
    private CerePlatformDiscountDAO cerePlatformDiscountDAO;

    @Autowired
    private CereBuyerPlatformDiscountVisitDAO cereBuyerPlatformDiscountVisitDAO;

    @Autowired
    private CereProductSkuService cereProductSkuService;

    @Override
    public CerePlatformDiscount queryPlatformDiscount(Long discountId) {
        return cerePlatformDiscountDAO.selectByPrimaryKey(discountId);
    }

    @Override
    public Page<PlatformDiscountProduct> queryPlatformDiscountProductList(PlatformDiscountParam param) {
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<PlatformDiscountProduct> list = cerePlatformDiscountDAO.queryPlatformDiscountProductList(param);
        PageInfo<PlatformDiscountProduct> pageInfo = new PageInfo<>(list);
        return new Page<>(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    public List<CanvasPlatformDiscount> getMinDiscount(RenovationParam param) {
        List<CanvasPlatformDiscount> list=cerePlatformDiscountDAO.getMinDiscount(param);
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(detail -> {
                //查询商品明细
                List<ToolProduct> distinctProducts = cerePlatformDiscountDAO.findDistinctProducts(detail.getDiscountId());
                if(!EmptyUtils.isEmpty(distinctProducts)){
                    distinctProducts.forEach(product -> {
                        //计算折扣价
                        BigDecimal discount=product.getDiscount().divide(BigDecimal.TEN,2,BigDecimal.ROUND_HALF_UP);
                        product.setPrice(product.getOriginalPrice().multiply(discount).setScale(2,BigDecimal.ROUND_HALF_UP));
                        product.setActivityType(IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode());
                    });
                }
                detail.setProducts(distinctProducts);
            });
        }
        return list;
    }

    @Override
    public CerePlatformDiscount findById(Long platformDiscountId) {
        return cerePlatformDiscountDAO.selectByPrimaryKey(platformDiscountId);
    }

    @Override
    public void insertVisit(CereBuyerPlatformDiscountVisit cereBuyerPlatformDiscountVisit) {
        cereBuyerPlatformDiscountVisitDAO.insert(cereBuyerPlatformDiscountVisit);
    }

    @Override
    public List<ShopPlatformDiscount> selectPlatformDiscountByShopIdList(List<Long> shopIdList) {
        return cerePlatformDiscountDAO.selectByShopIdList(shopIdList);
    }

    @Override
    public ProductDetail setActivityInfo(Long platformDiscountId, CereBuyerUser user, ProductDetail detail) {
        try {
            CerePlatformDiscount platformDiscount = cerePlatformDiscountDAO.selectByPrimaryKey(platformDiscountId);
            if (platformDiscount != null) {
                String time = TimeUtils.yyMMddHHmmss();
                detail.setIfAdd(platformDiscount.getIfAdd());
                detail.setStartTime(platformDiscount.getStartTime());
                detail.setEndTime(platformDiscount.getEndTime());
                BigDecimal price = detail.getOriginalPrice();
                if (IntegerEnum.ACTIVITY_START.getCode().equals(platformDiscount.getState())) {
                    detail.setTime(TimeUtils.getCountDownByTime(time,platformDiscount.getEndTime()));
                    detail.setIfEnable(IntegerEnum.NO.getCode());
                    price = price.multiply(platformDiscount.getDiscount()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
                } else if (TimeUtils.parseDate(platformDiscount.getSignEndTime(), "yyyy-MM-dd HH:mm:ss").before(new Date())) {
                    //当前时间已经是报名结束之间之后
                    detail.setIfEnable(IntegerEnum.YES.getCode());
                    price = price.multiply(platformDiscount.getDiscount()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
                }
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    price = BigDecimal.ZERO;
                }
                detail.setPlatformDiscountId(platformDiscountId);
                if(user!=null){
                    //新增浏览记录
                    CereBuyerPlatformDiscountVisit cereBuyerPlatformDiscountVisit=new CereBuyerPlatformDiscountVisit();
                    cereBuyerPlatformDiscountVisit.setBuyerUserId(user.getBuyerUserId());
                    cereBuyerPlatformDiscountVisit.setDiscountId(platformDiscountId);
                    cereBuyerPlatformDiscountVisit.setVisitTime(TimeUtils.yyMMddHHmmss());
                    cereBuyerPlatformDiscountVisit.setShopId(detail.getShopId());
                    cereBuyerPlatformDiscountVisitDAO.insert(cereBuyerPlatformDiscountVisit);
                }

                //设置库存总数 剩余库存数 原始价格 当前价格
                ProductStockInfo stockInfo = cerePlatformDiscountDAO.selectProductStockInfo(platformDiscountId, detail.getProductId());
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
                        //由于平台折扣是商品级别，所以全部sku的活动数据都和商品同步
                        sku.setActivityType(detail.getActivityType());
                        sku.setPlatformDiscountId(platformDiscountId);
                        sku.setStartTime(detail.getStartTime());
                        sku.setEndTime(detail.getEndTime());
                        sku.setTime(detail.getTime());
                        sku.setIfEnable(detail.getIfEnable());
                        sku.setTotal(detail.getTotal());
                        sku.setStockNumber(detail.getSurplusNumber());

                        if (IntegerEnum.ACTIVITY_START.getCode().equals(platformDiscount.getState())
                            || TimeUtils.parseDate(platformDiscount.getSignEndTime(), "yyyy-MM-dd HH:mm:ss").before(new Date())) {
                            //BigDecimal skuPrice = sku.getPrice();
                            //sku.setOriginalPrice(skuPrice);
                            sku.setPrice(sku.getOriginalPrice().multiply(platformDiscount.getDiscount()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
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
            log.error("setActivtyInfo platformDiscountId = {}, productId = {}, errorMessage = {}", platformDiscountId, detail.getProductId(), e.getMessage(), e);
        }
        return detail;
    }

    @Override
    public ProductDetail setActivityInfoForRealInfo(Long platformDiscountId, CereBuyerUser user, ProductDetail detail) {
        try {
            if (user != null) {
                //新增浏览记录
                CereBuyerPlatformDiscountVisit cereBuyerPlatformDiscountVisit=new CereBuyerPlatformDiscountVisit();
                cereBuyerPlatformDiscountVisit.setBuyerUserId(user.getBuyerUserId());
                cereBuyerPlatformDiscountVisit.setDiscountId(platformDiscountId);
                cereBuyerPlatformDiscountVisit.setVisitTime(TimeUtils.yyMMddHHmmss());
                cereBuyerPlatformDiscountVisit.setShopId(detail.getShopId());
                cereBuyerPlatformDiscountVisitDAO.insert(cereBuyerPlatformDiscountVisit);
            }

            //设置库存总数 剩余库存数 原始价格 当前价格
            ProductStockInfo stockInfo = cerePlatformDiscountDAO.selectProductStockInfo(platformDiscountId, detail.getProductId());
            if (stockInfo != null) {
                detail.setTotal(stockInfo.getTotal());
                detail.setSurplusNumber(stockInfo.getStockNumber());
            }
        } catch (Exception e) {
            log.error("setActivtyInfo platformDiscountId = {}, productId = {}, errorMessage = {}", platformDiscountId, detail.getProductId(), e.getMessage(), e);
        }
        return detail;
    }
}
