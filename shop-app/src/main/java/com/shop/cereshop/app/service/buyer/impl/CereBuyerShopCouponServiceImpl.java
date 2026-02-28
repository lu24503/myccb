/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer.impl;

import com.shop.cereshop.app.dao.buyer.CereBuyerShopCouponDAO;
import com.shop.cereshop.app.page.coupon.CommonCoupon;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.param.order.OrderProductParam;
import com.shop.cereshop.app.service.buyer.CereBuyerShopCouponService;
import com.shop.cereshop.commons.domain.buyer.CereBuyerShopCoupon;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CereBuyerShopCouponServiceImpl implements CereBuyerShopCouponService {

    @Autowired
    private CereBuyerShopCouponDAO cereBuyerShopCouponDAO;

    @Override
    public List<ProductCoupon> findCouponByProduct(BigDecimal total, Long buyerUserId, Long productId) {
        return cereBuyerShopCouponDAO.findCouponByProduct(total,buyerUserId,productId);
    }

    @Override
    public CereBuyerShopCoupon findById(Long id) {
        return cereBuyerShopCouponDAO.findById(id);
    }

    @Override
    public void updateState(CereBuyerShopCoupon cereBuyerShopCoupon) throws CoBusinessException {
        cereBuyerShopCouponDAO.updateState(cereBuyerShopCoupon);
    }

    @Override
    public void insert(CereBuyerShopCoupon cereBuyerShopCoupon) throws CoBusinessException {
        cereBuyerShopCouponDAO.insert(cereBuyerShopCoupon);
    }

    @Override
    public int findCount(Long buyerUserId, Long shopCouponId) {
        return cereBuyerShopCouponDAO.findCount(buyerUserId,shopCouponId);
    }

    @Override
    public List<ProductCoupon> getCoupons(Long buyerUserId,Integer state) {
        return cereBuyerShopCouponDAO.getCoupons(buyerUserId,state);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereBuyerShopCouponDAO.updateBuyerData(buyerUserId,id);
    }

    @Override
    public List<Long> findProductIds(Long shopCouponId) {
        return cereBuyerShopCouponDAO.findProductIds(shopCouponId);
    }

    @Override
    public List<CereBuyerShopCoupon> findByIds(List<OrderProductParam> shops) {
        return cereBuyerShopCouponDAO.findByIds(shops);
    }

    @Override
    public List<ProductCoupon> findCouponMatchCondition(Long buyerUserId, BigDecimal fullMoneyUpperLimit, List<Long> productIdList) {
        return cereBuyerShopCouponDAO.findCouponMatchCondition(buyerUserId, fullMoneyUpperLimit, productIdList, TimeUtils.yyMMddHHmmss());
    }

    @Override
    public List<CommonCoupon> selectTakeCount(Long buyerUserId, List<Long> couponIdList) {
        return cereBuyerShopCouponDAO.selectTakeCount(buyerUserId, couponIdList);
    }
}
