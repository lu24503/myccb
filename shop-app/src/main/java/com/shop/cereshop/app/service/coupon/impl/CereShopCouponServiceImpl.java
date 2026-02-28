/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.coupon.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.coupon.CereShopCouponDAO;
import com.shop.cereshop.app.page.coupon.CommonCoupon;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.param.canvas.CanvasCouponParam;
import com.shop.cereshop.app.param.coupon.ShopCouponParam;
import com.shop.cereshop.app.service.buyer.CereBuyerShopCouponService;
import com.shop.cereshop.app.service.coupon.CereShopCouponService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerShopCoupon;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.tool.CereShopCoupon;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CereShopCouponServiceImpl implements CereShopCouponService {

    @Autowired
    private CereShopCouponDAO cereShopCouponDAO;

    @Autowired
    private CereBuyerShopCouponService cereBuyerShopCouponService;

    @Override
    public List<ProductCoupon> findByProductId(Long shopId, Long productId) {
        return cereShopCouponDAO.findProductCanUseCoupon(shopId, productId);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void takeCoupon(ShopCouponParam param, CereBuyerUser user) throws CoBusinessException {
        String time = TimeUtils.yyMMddHHmmss();
        //查询优惠券数据
        CereShopCoupon cereShopCoupon=cereShopCouponDAO.selectByPrimaryKey(param.getShopCouponId());
        if(cereShopCoupon!=null){
            CereBuyerShopCoupon cereBuyerShopCoupon=new CereBuyerShopCoupon();
            cereBuyerShopCoupon.setState(IntegerEnum.COUPON_HAVE.getCode());
            cereBuyerShopCoupon.setReduceMoney(cereShopCoupon.getCouponContent());
            cereBuyerShopCoupon.setFullMoney(cereShopCoupon.getThreshold());
            cereBuyerShopCoupon.setStartTime(cereShopCoupon.getEffectiveStart());
            cereBuyerShopCoupon.setEndTime(cereShopCoupon.getEffectiveEnd());
            cereBuyerShopCoupon.setCouponName(cereShopCoupon.getCouponName());
            cereBuyerShopCoupon.setCreateTime(time);
            cereBuyerShopCoupon.setShopCouponId(cereShopCoupon.getShopCouponId());
            cereBuyerShopCoupon.setBuyerUserId(user.getBuyerUserId());
            cereBuyerShopCoupon.setCouponType(cereShopCoupon.getCouponType());
            cereBuyerShopCouponService.insert(cereBuyerShopCoupon);
            //更新发行数量
            cereShopCoupon.setNumber(cereShopCoupon.getNumber()-1);
            cereShopCouponDAO.updateByPrimaryKeySelective(cereShopCoupon);
        }
    }

    @Override
    public CereShopCoupon findById(Long shopCouponId) {
        return cereShopCouponDAO.selectByPrimaryKey(shopCouponId);
    }

    @Override
    public void updateByPrimaryKeySelective(CereShopCoupon cereShopCoupon) throws CoBusinessException {
        cereShopCouponDAO.updateByPrimaryKeySelective(cereShopCoupon);
    }

    @Override
    public Page getShopCoupons(CanvasCouponParam param,CereBuyerUser user) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<ProductCoupon> list=null;
        if(user==null){
            list=cereShopCouponDAO.getShopCoupons(param);
        }else {
            param.setBuyerUserId(user.getBuyerUserId());
            list=cereShopCouponDAO.getShopCouponsByUserId(param);
            if(!EmptyUtils.isEmpty(list)){
                //设置优惠券状态
                for (int i = 0; i < list.size(); i++) {
                    if(i<0){
                        i=0;
                    }
                    ProductCoupon shopCoupon=list.get(i);
                    if(shopCoupon.getCount()<=0&&shopCoupon.getStockNumber()<=0){
                        //如果未领取过且库存数量不足,过滤优惠券
                        list.remove(i);
                        i--;
                        continue;
                    }
                    if(!EmptyUtils.isEmpty(shopCoupon.getFrequency())){
                        //如果有限制领取次数
                        if(shopCoupon.getFrequency().equals(shopCoupon.getCount())){
                            //如果限制领取次数=用户已领取优惠券总数,修改店铺优惠券状态为已领取
                            shopCoupon.setState(IntegerEnum.COUPON_HAVE.getCode());
                        }
                    }else {
                        //如果没有限制次数且领取总数大于0且库存数量不足
                        if(shopCoupon.getCount()>0&&shopCoupon.getStockNumber()<=0){
                            shopCoupon.setState(IntegerEnum.COUPON_HAVE.getCode());
                        }
                    }
                }
            }
        }
        if(!EmptyUtils.isEmpty(list)){
            for (ProductCoupon coupon : list) {
                if(IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(coupon.getCouponType())){
                    //满减设置内容
                    if(!EmptyUtils.isEmptyBigdecimal(coupon.getThreshold())){
                        coupon.setContent("满"+coupon.getThreshold().stripTrailingZeros().toPlainString()+"减"
                                +coupon.getCouponContent().stripTrailingZeros().toPlainString()+"元");
                    }else {
                        coupon.setContent("无门槛减"
                                +coupon.getCouponContent().stripTrailingZeros().toPlainString()+"元");
                    }
                }else {
                    //折扣设置内容
                    if(!EmptyUtils.isEmptyBigdecimal(coupon.getThreshold())){
                        coupon.setContent("满"+coupon.getThreshold().stripTrailingZeros().toPlainString()+"元"
                                +coupon.getCouponContent().stripTrailingZeros().toPlainString()+"折");
                    }else {
                        coupon.setContent("无门槛"
                                +coupon.getCouponContent().stripTrailingZeros().toPlainString()+"折");
                    }
                }
            }
        }
        PageInfo<ProductCoupon> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public List<ProductCoupon> findByProductIdAndUserId(Long buyerUserId, Long shopId, Long productId) {
        List<ProductCoupon> couponList = cereShopCouponDAO.findProductCanUseCoupon(shopId, productId);
        if (CollectionUtils.isNotEmpty(couponList)) {
            List<Long> couponIdList =  couponList.stream().map(ProductCoupon::getShopCouponId).collect(Collectors.toList());
            List<CommonCoupon> takeCountList = cereBuyerShopCouponService.selectTakeCount(buyerUserId, couponIdList);
            Map<Long, Integer> takeCountMap = takeCountList.stream().collect(Collectors.toMap(CommonCoupon::getCouponId, CommonCoupon::getUserTakeCount));
            for (ProductCoupon coupon:couponList) {
                coupon.setCount(takeCountMap.getOrDefault(coupon.getShopCouponId(), 0));
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
    public List<CereShopCoupon> findOnGoingCouponByBatchId(List<Long> couponIdList) {
        return cereShopCouponDAO.findOnGoingCouponByBatchId(couponIdList);
    }

    @Override
    public List<CereBuyerUser> selectCanTakeCouponUser(Long shopId, Long couponId, Integer receiveType, Integer frequency) {
        return cereShopCouponDAO.selectCanTakeCouponUser(shopId, couponId, receiveType, frequency);
    }
}
