/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.activity.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.activity.CereBuyerCouponDAO;
import com.shop.cereshop.app.dao.activity.CerePlatformActivityDAO;
import com.shop.cereshop.app.page.canvas.CanvasCoupon;
import com.shop.cereshop.app.page.coupon.CommonCoupon;
import com.shop.cereshop.app.page.coupon.CreditCoupon;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.param.canvas.CanvasCouponParam;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.app.param.groupwork.GroupWorkProductParam;
import com.shop.cereshop.app.service.activity.CerePlatformActivityService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.activity.CereBuyerCoupon;
import com.shop.cereshop.commons.domain.activity.CerePlatformActivity;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CerePlatformActivityServiceImpl implements CerePlatformActivityService {

    @Autowired
    private CerePlatformActivityDAO cerePlatformActivityDAO;

    @Autowired
    private CereBuyerCouponDAO cereBuyerCouponDAO;

    @Override
    public Page getCoupons(CanvasCouponParam param, CereBuyerUser user) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<CanvasCoupon> list=null;
        if(user!=null){
            param.setBuyerUserId(user.getBuyerUserId());
            list = cerePlatformActivityDAO.getCoupons(param);
            if(!EmptyUtils.isEmpty(list)){
                //过滤已使用和已过期的优惠券
             /*   list=list.stream()
                        .filter(productCoupon -> IntegerEnum.COUPON_NOT_HAVE.getCode().equals(productCoupon.getState())
                                ||IntegerEnum.COUPON_HAVE.getCode().equals(productCoupon.getState()))
                        .collect(Collectors.toList());*/
                // 需要删除的优惠券
                List<Long> needRemoveCoupon = new ArrayList();
                for (CanvasCoupon coupon:list) {
                    /**
                     * COUPON_HAVE("优惠券状态-已领取",0),
                     *     COUPON_USE("优惠券状态-已使用",1),
                     *     COUPON_OVERDUE("优惠券状态-已过期",2),
                     *     COUPON_NOT_HAVE("优惠券状态-未领取",3),
                     */
                    LambdaQueryWrapper<CereBuyerCoupon> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(CereBuyerCoupon::getBuyerUserId, user.getBuyerUserId());
                    wrapper.eq(CereBuyerCoupon::getActivityId, coupon.getCouponId());
                    // 根据优惠券id获取用户已经领取的优惠券list
                    List<CereBuyerCoupon> buyerCouponList = cereBuyerCouponDAO.selectList(wrapper);
                    long overdueCount = buyerCouponList.stream().filter(c->IntegerEnum.COUPON_OVERDUE.getCode().equals(c.getState())).count();
                    long useCount = buyerCouponList.stream().filter(c->IntegerEnum.COUPON_USE.getCode().equals(c.getState())).count();
                    if (overdueCount > 0) {
                        coupon.setState(IntegerEnum.COUPON_OVERDUE.getCode());
                    } else if (useCount > 0) {
                        coupon.setState(IntegerEnum.COUPON_USE.getCode());
                    } else if (buyerCouponList.size() > 0) {
                        coupon.setState(IntegerEnum.COUPON_HAVE.getCode());
                        // 判断优惠券是否超过单用户最多领取数量
                        if(coupon.getFrequency()!=null&&buyerCouponList.size()>=coupon.getFrequency()){
                            needRemoveCoupon.add(coupon.getCouponId());
                        }
                    }
                }

                // 去掉不可领取的优惠券
                list = list.stream().filter(coupon->!(needRemoveCoupon.contains(coupon.getCouponId()))).collect(Collectors.toList());
            }
        }else {
            list = cerePlatformActivityDAO.getCoupons(param);
        }
        if(!EmptyUtils.isEmpty(list)){
            //设置内容
            list.forEach(canvasCoupon -> {
                if (IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(canvasCoupon.getDiscountMode())) {
                    canvasCoupon.setContent("满"+canvasCoupon.getFullMoney().stripTrailingZeros().toPlainString()+"减"
                            +canvasCoupon.getReduceMoney().stripTrailingZeros().toPlainString()+"元");
                } else {
                    canvasCoupon.setContent(canvasCoupon.getReduceMoney().stripTrailingZeros().toPlainString()+"折券");
                }
            });
        }
        PageInfo<CanvasCoupon> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public List<ProductCoupon> findCouponByProductId(Long productId) {
        return cerePlatformActivityDAO.findCouponByProductId(productId);
    }

    @Override
    public List<ProductCoupon> findCouponByProductIdAndUserId(Long buyerUserId, Long productId) {
        List<ProductCoupon> couponList = cerePlatformActivityDAO.findProductCanUseCoupon(productId);
        if (CollectionUtils.isNotEmpty(couponList)) {
            List<Long> couponIdList =  couponList.stream().map(ProductCoupon::getCouponId).collect(Collectors.toList());
            List<CommonCoupon> takeCountList = cereBuyerCouponDAO.selectTakeCount(buyerUserId, couponIdList);
            Map<Long, Integer> takeCountMap = takeCountList.stream().collect(Collectors.toMap(CommonCoupon::getCouponId, CommonCoupon::getUserTakeCount));
            for (ProductCoupon coupon:couponList) {
                coupon.setCount(takeCountMap.getOrDefault(coupon.getCouponId(), 0));
                if (coupon.getFrequency() != null && coupon.getFrequency() > 0 && coupon.getCount() >= coupon.getFrequency()) {
                    coupon.setState(IntegerEnum.COUPON_HAVE.getCode());
                } else {
                    coupon.setState(IntegerEnum.COUPON_NOT_HAVE.getCode());
                }
            }
        }
        return couponList;
    }

    @Override
    public CerePlatformActivity findById(Long couponId) {
        return cerePlatformActivityDAO.selectByPrimaryKey(couponId);
    }

    @Override
    public void updateByPrimaryKeySelective(CerePlatformActivity cerePlatformActivity) throws CoBusinessException {
        cerePlatformActivityDAO.updateByPrimaryKeySelective(cerePlatformActivity);
    }

    @Override
    public List<CerePlatformActivity> findOnGoingCouponByBatchId(List<Long> couponIdList) {
        return cerePlatformActivityDAO.findOnGoingCouponByBatchId(couponIdList);
    }

    @Override
    public List<ToolProduct> getGroupWorkProducts(RenovationParam param) {
        List<ToolProduct> groupWorkProducts = cerePlatformActivityDAO.getGroupWorkProducts(param);
        groupWorkProducts.forEach(s->{
            GroupWorkProductParam groupWorkProductParam=new GroupWorkProductParam();
            groupWorkProductParam.setProductId(s.getProductId());
            groupWorkProductParam.setShopGroupWorkId(s.getShopGroupWorkId());
            Integer workUsers = cerePlatformActivityDAO.getGroupWorkProductCount(groupWorkProductParam);
            s.setWorkUsers(workUsers);
        });
        return groupWorkProducts;
    }

    @Override
    public Page<CreditCoupon> selectCreditCouponList(Long buyerUserId, PageParam param) {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<CreditCoupon> list = cerePlatformActivityDAO.selectCreditCouponList(buyerUserId);
        PageInfo<CreditCoupon> pageInfo=new PageInfo<>(list);
        Page<CreditCoupon> page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public List<Long> findProductIdList(Long couponId) {
        return cerePlatformActivityDAO.findProductIdList(couponId);
    }
}
