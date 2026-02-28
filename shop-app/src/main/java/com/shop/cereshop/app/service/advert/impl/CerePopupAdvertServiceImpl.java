/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.advert.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.cereshop.app.dao.activity.CereBuyerCouponDAO;
import com.shop.cereshop.app.dao.advert.CereCloseAdvertDAO;
import com.shop.cereshop.app.dao.advert.CerePopupAdvertDAO;
import com.shop.cereshop.app.param.advert.PopupAdvertParam;
import com.shop.cereshop.app.param.coupon.CouponTakeState;
import com.shop.cereshop.app.service.advert.CerePopupAdvertService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.activity.CereBuyerCoupon;
import com.shop.cereshop.commons.domain.advert.CereCloseAdvert;
import com.shop.cereshop.commons.domain.advert.CerePopupAdvert;
import com.shop.cereshop.commons.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 业务实现类
 * 弹窗广告表
 * </p>
 *
 * @author JustArgo
 * @date 2021-12-04
 */
@Slf4j
@Service
public class CerePopupAdvertServiceImpl implements CerePopupAdvertService {

    @Autowired
    private CerePopupAdvertDAO cerePopupAdvertDAO;

    @Autowired
    private CereCloseAdvertDAO cereCloseAdvertDAO;

    @Autowired
    private CereBuyerCouponDAO cereBuyerCouponDAO;

    @Override
    public List<PopupAdvertParam> selectByCondition(Long buyerUserId, Integer condition) {
        String now = TimeUtils.yyMMddHHmmss();
        List<PopupAdvertParam> paramList = new ArrayList<>();
        List<CerePopupAdvert> advertList = cerePopupAdvertDAO.selectByCondition(buyerUserId, condition, now, now);
        for (CerePopupAdvert advert:advertList) {
            PopupAdvertParam param = new PopupAdvertParam();
            BeanUtils.copyProperties(advert, param);
            paramList.add(param);
        }
        if (buyerUserId != null) {
            for (PopupAdvertParam advert:paramList) {
                if (IntegerEnum.JUMP_TYPE_COUPON.getCode().equals(advert.getJumpType())) {
                    List<CouponTakeState> takeStateList = new ArrayList<>();
                    JSONObject jumpObj = JSONObject.parseObject(advert.getJumpContent());
                    String couponIdStr = jumpObj.getString("items");
                    if (StringUtils.isNotBlank(couponIdStr)) {
                        String[] arr = couponIdStr.split(",");
                        for (int i=0;i<arr.length;i++) {
                            Long couponId = Long.valueOf(arr[i]);
                            LambdaQueryWrapper<CereBuyerCoupon> wrapper = new LambdaQueryWrapper<>();
                            wrapper.eq(CereBuyerCoupon::getActivityId, couponId);
                            wrapper.eq(CereBuyerCoupon::getBuyerUserId, buyerUserId);
                            List<CereBuyerCoupon> buyerCouponList = cereBuyerCouponDAO.selectList(wrapper);
                            CouponTakeState takeState = new CouponTakeState();
                            takeState.setCouponId(couponId);
                            if (CollectionUtils.isNotEmpty(buyerCouponList)) {
                                boolean used = buyerCouponList.stream().anyMatch(o->IntegerEnum.COUPON_USE.getCode().equals(o.getState()));
                                boolean expired = buyerCouponList.stream().anyMatch(o->IntegerEnum.COUPON_OVERDUE.getCode().equals(o.getState()));
                                if (used) {
                                    takeState.setState(IntegerEnum.COUPON_USE.getCode());
                                } else if (expired) {
                                    takeState.setState(IntegerEnum.COUPON_OVERDUE.getCode());
                                } else {
                                    takeState.setState(IntegerEnum.COUPON_NOT_HAVE.getCode());
                                }
                            } else {
                                takeState.setState(IntegerEnum.COUPON_NOT_HAVE.getCode());
                            }
                            takeStateList.add(takeState);
                        }
                        advert.setCouponTakeStateList(takeStateList);
                    }
                }
            }
        }
        return paramList;
    }

    @Override
    public Integer closeAdvert(CereCloseAdvert param) {
        String time = TimeUtils.yyMMddHHmmss();
        param.setCreateTime(time);
        param.setUpdateTime(time);
        param.setCloseDate(LocalDate.now());
        return cereCloseAdvertDAO.insert(param);
    }
}
