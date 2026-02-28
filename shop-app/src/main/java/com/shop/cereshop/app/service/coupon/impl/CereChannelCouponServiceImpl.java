/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.coupon.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.cereshop.app.dao.buyer.CereBuyerShopCouponDAO;
import com.shop.cereshop.app.dao.coupon.CereChannelCouponDAO;
import com.shop.cereshop.app.dao.product.CereProductImageDAO;
import com.shop.cereshop.app.param.coupon.ChannelCouponDetail;
import com.shop.cereshop.app.service.coupon.CereChannelCouponService;
import com.shop.cereshop.app.service.product.CereShopProductService;
import com.shop.cereshop.app.service.shop.CerePlatformShopservice;
import com.shop.cereshop.commons.cache.product.ProductBo;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerShopCoupon;
import com.shop.cereshop.commons.domain.product.CereProductImage;
import com.shop.cereshop.commons.domain.tool.CereChannelCoupon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 渠道券表
 * </p>
 *
 * @date 2021-12-12
 */
@Slf4j
@Service
public class CereChannelCouponServiceImpl implements CereChannelCouponService {

    @Autowired
    private CereChannelCouponDAO cereChannelCouponDAO;

    @Autowired
    private CereProductImageDAO cereProductImageDAO;

    @Autowired
    private CerePlatformShopservice cerePlatformShopService;

    @Autowired
    private CereShopProductService cereShopProductService;

    @Autowired
    private CereBuyerShopCouponDAO cereBuyerShopCouponDAO;

    @Override
    public ChannelCouponDetail getChannelCouponDetail(CereChannelCoupon param, Long buyerUserId) {
        ChannelCouponDetail detail = cereChannelCouponDAO.getChannelCouponDetail(param);
        if (detail != null) {
            //设置商品图像
            List<CereProductImage> productImageList = cereProductImageDAO.selectByProductId(detail.getProductId());
            List<String> imgList = productImageList.stream().map(CereProductImage::getProductImage).collect(Collectors.toList());
            detail.setProductImageList(imgList);

            ProductBo productBo = cereShopProductService.fetchProductCache(detail.getShopId(), detail.getProductId(), detail.getSkuId(), null, false);
            if (productBo != null) {
                detail.setOriginalPrice(productBo.getPrice());
            }

            //设置价格
            if (IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(detail.getCouponType())) {
                if (detail.getOriginalPrice().compareTo(detail.getFullMoney()) >= 0) {
                    detail.setPrice(detail.getOriginalPrice().subtract(detail.getReduceMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            } else {
                BigDecimal actualDiscount = detail.getReduceMoney().divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
                detail.setPrice(detail.getOriginalPrice().multiply(actualDiscount).setScale(2, BigDecimal.ROUND_HALF_UP));
            }

            //设置购买人数
            Integer buyUserCount = cerePlatformShopService.findPayUsers(detail.getProductId());
            detail.setBuyUserCount(buyUserCount);

            //设置领取状态
            if (buyerUserId != null) {
                LambdaQueryWrapper<CereBuyerShopCoupon> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(CereBuyerShopCoupon::getBuyerUserId, buyerUserId);
                wrapper.eq(CereBuyerShopCoupon::getShopCouponId, detail.getShopCouponId());
                List<CereBuyerShopCoupon> buyerShopCouponList = cereBuyerShopCouponDAO.selectList(wrapper);
                long overdueCount = buyerShopCouponList.stream().filter(c->IntegerEnum.COUPON_OVERDUE.getCode().equals(c.getState())).count();
                long useCount = buyerShopCouponList.stream().filter(c->IntegerEnum.COUPON_USE.getCode().equals(c.getState())).count();
                if (overdueCount > 0) {
                    detail.setState(IntegerEnum.COUPON_OVERDUE.getCode());
                } else if (useCount > 0) {
                    detail.setState(IntegerEnum.COUPON_USE.getCode());
                } else if (buyerShopCouponList.size() > 0) {
                    detail.setState(IntegerEnum.COUPON_HAVE.getCode());
                }
            }
        }
        return detail;
    }

}
