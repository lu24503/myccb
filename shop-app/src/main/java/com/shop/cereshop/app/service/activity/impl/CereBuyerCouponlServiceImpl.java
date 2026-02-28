/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.activity.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.activity.CereBuyerCouponDAO;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.param.coupon.ActivityParam;
import com.shop.cereshop.app.param.coupon.CouponListParam;
import com.shop.cereshop.app.param.coupon.CouponParam;
import com.shop.cereshop.app.param.coupon.OrderCouponParam;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.app.service.activity.CereBuyerCouponService;
import com.shop.cereshop.app.service.activity.CerePlatformActivityService;
import com.shop.cereshop.app.service.buyer.CereBuyerShopCouponService;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.coupon.CereShopCouponService;
import com.shop.cereshop.app.service.redis.CereRedisKeyServcice;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.CreditOptTypeEnum;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.constant.StringEnum;
import com.shop.cereshop.commons.domain.activity.CereBuyerCoupon;
import com.shop.cereshop.commons.domain.activity.CerePlatformActivity;
import com.shop.cereshop.commons.domain.buyer.CereBuyerShopCoupon;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.tool.CereShopCoupon;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CereBuyerCouponlServiceImpl implements CereBuyerCouponService {

    @Autowired
    private CereBuyerCouponDAO cereBuyerCouponDAO;

    @Autowired
    private StringRedisService stringRedisService;

    @Autowired
    private CereRedisKeyServcice cereRedisKeyServcice;

    @Autowired
    private CerePlatformActivityService cerePlatformActivityService;

    @Autowired
    private CereShopCouponService cereShopCouponService;

    @Autowired
    private CereBuyerShopCouponService cereBuyerShopCouponService;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void takeCoupon(CouponParam param, CereBuyerUser user) throws CoBusinessException {
        takeCouponInner(param, user);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void takeBatchCoupon(List<CouponParam> paramList, CereBuyerUser user) throws CoBusinessException {
        for (CouponParam param:paramList) {
            takeCouponInner(param, user);
        }
    }

    @Override
    public CereBuyerCoupon findLatestUsedCoupon(Long couponId, Long buyerUserId) {
        return cereBuyerCouponDAO.findLatestUsedCoupon(couponId, buyerUserId);
    }

    @Override
    public List<ProductCoupon> findCouponMatchCondition(Long buyerUserId, BigDecimal fullMoneyUpperLimit, List<Long> productIdList) {
        return cereBuyerCouponDAO.findCouponMatchCondition(buyerUserId, fullMoneyUpperLimit, productIdList, TimeUtils.yyMMddHHmmss());
    }

    private void takeCouponInner(CouponParam param, CereBuyerUser user) throws CoBusinessException {
        String time = TimeUtils.yyMMddHHmmss();
        if(!EmptyUtils.isLongEmpty(param.getCouponId())){
            //平台优惠券领取,查询优惠券数据
            CerePlatformActivity cerePlatformActivity=cerePlatformActivityService.findById(param.getCouponId());
            if(cerePlatformActivity!=null){
                //校验是否过期
                try {
                    if (cerePlatformActivity.getActivityEndTime() != null){
                        if (!TimeUtils.compareAfterTime(cerePlatformActivity.getActivityEndTime())) {
                            throw new CoBusinessException(CoReturnFormat.COUPON_EXPIRED);
                        }
                    }
                } catch (Exception e) {
                    log.error("parseTime error: " + e.getMessage(), e);
                    throw new CoBusinessException(CoReturnFormat.TIME_ERROR);
                }
                //校验剩余数量是否充足
                if(cerePlatformActivity.getStockNumber()<=0){
                    throw new CoBusinessException(CoReturnFormat.COUPON_RECEIVE_FINISH);
                }
                if(IntegerEnum.COUPON_RECEIVE_TYPE_LIMITED.getCode().equals(cerePlatformActivity.getReceiveType())){
                    //限制领取次数,校验是否超出领取限制
                    //查询已领取次数
                    int count = cereBuyerCouponDAO.findCount(user.getBuyerUserId(), cerePlatformActivity.getActivityId());
                    if(cerePlatformActivity.getFrequency()-count<1){
                        throw new CoBusinessException(CoReturnFormat.COUPON_TAKE_UPPER_LIMIT);
                    }
                }
                CereBuyerCoupon cereBuyerCoupon=new CereBuyerCoupon();
                cereBuyerCoupon.setBuyerUserId(user.getBuyerUserId());
                cereBuyerCoupon.setCreateTime(time);
                cereBuyerCoupon.setUpdateTime(time);
                cereBuyerCoupon.setState(IntegerEnum.COUPON_HAVE.getCode());
                cereBuyerCoupon.setCouponId(cerePlatformActivity.getActivityId());
                cereBuyerCoupon.setActivityId(cerePlatformActivity.getActivityId());
                cereBuyerCoupon.setActivityName(cerePlatformActivity.getActivityName());
                cereBuyerCoupon.setDiscountMode(cerePlatformActivity.getDiscountMode());
                cereBuyerCoupon.setDiscountProgramme(1);
                cereBuyerCoupon.setEndTime(cerePlatformActivity.getActivityEndTime());
                cereBuyerCoupon.setFullMoney(cerePlatformActivity.getThreshold());
                cereBuyerCoupon.setReduceMoney(cerePlatformActivity.getCouponContent());
                cereBuyerCoupon.setCouponCode(param.getCouponCode());
                cereBuyerCoupon.setSource(param.getSource());
                cereBuyerCoupon.setStartTime(cerePlatformActivity.getActivityStartTime());
                cereBuyerCouponDAO.insert(cereBuyerCoupon);
                // 如果是积分兑换，需要记录积分记录
                if (cerePlatformActivity.getIfCredit() != null
                        && IntegerEnum.YES.getCode().equals(cerePlatformActivity.getIfCredit())
                        && param.getSource() != null
                        && IntegerEnum.COUPON_SOURCE_CREDIT_EXCHANGE.getCode().equals(param.getSource())) {
                    cereBuyerUserService.decreaseCredit(user.getBuyerUserId(), cerePlatformActivity.getCredit(), CreditOptTypeEnum.EXCHANGE_COUPON);
                }

                //更新库存数量
                cerePlatformActivity.setStockNumber(cerePlatformActivity.getStockNumber()-1);
                cerePlatformActivityService.updateByPrimaryKeySelective(cerePlatformActivity);
                try {
                    //新增redis延时任务修改优惠券状态为已过期
                    stringRedisService.set(StringEnum.COUPON_OVERDUE.getCode()+"-"+cereBuyerCoupon.getBuyerUserId()
                            +"-"+cereBuyerCoupon.getActivityId()+"-"+cereBuyerCoupon.getFullMoney(),1,TimeUtils.getCountDownByTime(time,cereBuyerCoupon.getEndTime()));
                    //新增redis记录数据
                    cereRedisKeyServcice.add(StringEnum.COUPON_OVERDUE.getCode()+"-"+cereBuyerCoupon.getBuyerUserId()
                            +"-"+cereBuyerCoupon.getActivityId()+"-"+cereBuyerCoupon.getFullMoney(),cereBuyerCoupon.getEndTime());
                } catch (ParseException e) {
                    log.error("parseTime error: " + e.getMessage(), e);
                    throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
                }
            }
        }else if(!EmptyUtils.isLongEmpty(param.getShopCouponId())){
            //店铺优惠券领取,查询优惠券数据
            CereShopCoupon cereShopCoupon=cereShopCouponService.findById(param.getShopCouponId());
            if(cereShopCoupon!=null){
                //校验库存数量是否充足
                if(cereShopCoupon.getStockNumber()<=0){
                    throw new CoBusinessException(CoReturnFormat.COUPON_RECEIVE_FINISH);
                }
                if(IntegerEnum.COUPON_RECEIVE_TYPE_LIMITED.getCode().equals(cereShopCoupon.getReceiveType())){
                    //限制领取次数,校验是否超出领取限制
                    //查询已领取次数
                    int count = cereBuyerShopCouponService.findCount(user.getBuyerUserId(), cereShopCoupon.getShopCouponId());
                    if(cereShopCoupon.getFrequency()-count<1){
                        throw new CoBusinessException(CoReturnFormat.COUPON_TAKE_UPPER_LIMIT);
                    }
                }
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
                //更新库存数量
                cereShopCoupon.setStockNumber(cereShopCoupon.getStockNumber()-1);
                cereShopCouponService.updateByPrimaryKeySelective(cereShopCoupon);
                try {
                    //新增redis延时任务修改优惠券状态为已过期
                    stringRedisService.set(StringEnum.SHOP_COUPON_OVERDUE.getCode()+"-"+cereBuyerShopCoupon.getId()
                            ,1,TimeUtils.getCountDownByTime(time,cereBuyerShopCoupon.getEndTime()));
                    //新增redis记录数据
                    cereRedisKeyServcice.add(StringEnum.SHOP_COUPON_OVERDUE.getCode()+"-"+cereBuyerShopCoupon.getId()
                            ,cereBuyerShopCoupon.getEndTime());
                } catch (ParseException e) {
                    log.error("parseTime error: " + e.getMessage(), e);
                    throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
                }
            }
        }
    }

    @Override
    public List<ProductCoupon> getCoupons(Long buyerUserId) throws CoBusinessException {
        List<ProductCoupon> coupons=new ArrayList<>();
        //查询平台优惠券
        List<ProductCoupon> list=cereBuyerCouponDAO.getCoupons(buyerUserId,0);
        //查询店铺优惠券
        List<ProductCoupon> couponList=cereBuyerShopCouponService.getCoupons(buyerUserId,0);
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(productCoupon -> {
                try {
                    if(TimeUtils.compareAfterTime(productCoupon.getEndTime())){
                        if(IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(productCoupon.getCouponType())) {
                            productCoupon.setContent("满"+productCoupon.getFullMoney().stripTrailingZeros().toPlainString()+
                                    "减"+productCoupon.getReduceMoney().stripTrailingZeros().toPlainString()+"元");
                        } else {
                            productCoupon.setContent(productCoupon.getReduceMoney().stripTrailingZeros().toPlainString()+"折券");
                        }
                        coupons.add(productCoupon);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        if(!EmptyUtils.isEmpty(couponList)){
            couponList.forEach(productCoupon -> {
                try {
                    if(TimeUtils.compareAfterTime(productCoupon.getEndTime())){
                        if(IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(productCoupon.getCouponType())){
                            productCoupon.setContent("满"+productCoupon.getFullMoney().stripTrailingZeros().toPlainString()+
                                    "减"+productCoupon.getReduceMoney().stripTrailingZeros().toPlainString()+"元");
                        }else {
                            productCoupon.setContent(productCoupon.getReduceMoney().stripTrailingZeros().toPlainString()+"折券");
                        }
                        coupons.add(productCoupon);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        coupons.sort((o1, o2) -> {
            return o2.getCreateTime().compareTo(o1.getCreateTime());
        });
        return coupons;
    }

    @Override
    public Page getCouponProducts(ActivityParam param) throws CoBusinessException {
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<Product> list = null;
        if (!EmptyUtils.isLongEmpty(param.getActivityId())) {
            //平台优惠券商品查询
            list = cereBuyerCouponDAO.getCouponProducts(param);
        } else {
            //店铺优惠券商品查询
            list = cereBuyerCouponDAO.getShopCouponProducts(param);
        }
        PageInfo<Product> pageInfo = new PageInfo<>(list);
        Page page = new Page(pageInfo.getList(), pageInfo.getTotal());
        return page;
    }

    @Override
    public List<ProductCoupon> getOrderCoupons(OrderCouponParam param, CereBuyerUser user) throws CoBusinessException {
        List<ProductCoupon> list=new ArrayList<>();
        if(!EmptyUtils.isEmpty(param.getParams())){
            param.getParams().forEach(shop -> {
                if(!EmptyUtils.isEmpty(shop.getProducts())){
                    shop.getProducts().forEach(product -> {
                        //根据店铺id、商品id、商品价格查询满足条件的优惠券
                        List<ProductCoupon> productCoupons=cereBuyerCouponDAO.findCouponByProduct(product.getPrice(),
                                user.getBuyerUserId(),product.getProductId());
                        if(!EmptyUtils.isEmpty(productCoupons)){
                            productCoupons.forEach(coupon ->{
                                //拼接活动id、满减金额字符串以便去重
                                coupon.setDistinct(coupon.getActivityId()+"-"+coupon.getFullMoney());
                                list.add(coupon);
                            });
                        }
                    });
                }
            });
        }
        List<ProductCoupon> collect=null;
        if(!EmptyUtils.isEmpty(list)){
            //去除重复活动和对应金额优惠券
            collect= list.stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(
                            () -> new TreeSet<>(Comparator.comparing(data -> data.getDistinct()))), ArrayList::new)
            );
        }
        return collect;
    }

    @Override
    public void updateState(CereBuyerCoupon buyerCoupon) throws CoBusinessException {
        cereBuyerCouponDAO.updateState(buyerCoupon);
    }

    @Override
    public List<ProductCoupon> findCouponByProduct(BigDecimal price, Long buyerUserId, Long productId) {
        return cereBuyerCouponDAO.findCouponByProduct(price,buyerUserId,productId);
    }

    @Override
    public List<ProductCoupon> getCouponList(CouponListParam param, CereBuyerUser user) throws CoBusinessException {
        List<ProductCoupon> coupons=new ArrayList<>();
        //查询平台优惠券
        List<ProductCoupon> list=cereBuyerCouponDAO.getCoupons(user.getBuyerUserId(),param.getState());
        //查询店铺优惠券
        List<ProductCoupon> couponList=cereBuyerShopCouponService.getCoupons(user.getBuyerUserId(),param.getState());
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(productCoupon -> {
                if(IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(productCoupon.getCouponType())) {
                    productCoupon.setContent("满"+productCoupon.getFullMoney().stripTrailingZeros().toPlainString()+
                            "减"+productCoupon.getReduceMoney().stripTrailingZeros().toPlainString()+"元");
                } else {
                    productCoupon.setContent(productCoupon.getReduceMoney().stripTrailingZeros().toPlainString()+"折券");
                }
                coupons.add(productCoupon);
            });
        }
        if(!EmptyUtils.isEmpty(couponList)){
            couponList.forEach(productCoupon -> {
                if(IntegerEnum.COUPON_TYPE_REDUTION.getCode().equals(productCoupon.getCouponType())){
                    productCoupon.setContent("满"+productCoupon.getFullMoney().stripTrailingZeros().toPlainString()+
                            "减"+productCoupon.getReduceMoney().stripTrailingZeros().toPlainString()+"元");
                }else {
                    productCoupon.setContent(productCoupon.getReduceMoney().stripTrailingZeros().toPlainString()+"折券");
                }
                coupons.add(productCoupon);
            });
        }
        return coupons;
    }

    @Override
    public CereBuyerCoupon findByCouponId(Long couponId,Long buyerUserId) {
        return cereBuyerCouponDAO.findByCouponId(couponId,buyerUserId);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereBuyerCouponDAO.updateBuyerData(buyerUserId,id);
    }

    @Override
    public void updateByUserIdAndCouponId(CereBuyerCoupon cereBuyerCoupon) throws CoBusinessException {
        cereBuyerCouponDAO.updateByUserIdAndCouponId(cereBuyerCoupon);
    }
}
