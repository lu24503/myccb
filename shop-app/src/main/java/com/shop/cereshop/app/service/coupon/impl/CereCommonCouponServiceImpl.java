package com.shop.cereshop.app.service.coupon.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.activity.CereBuyerCouponDAO;
import com.shop.cereshop.app.dao.buyer.CereBuyerShopCouponDAO;
import com.shop.cereshop.app.dao.channel.ShopChannelActivityCouponDAO;
import com.shop.cereshop.app.dao.channel.ShopChannelActivityDAO;
import com.shop.cereshop.app.dao.coupon.CereCommonCouponDAO;
import com.shop.cereshop.app.page.coupon.CommonCoupon;
import com.shop.cereshop.app.param.coupon.ActivityParam;
import com.shop.cereshop.app.param.coupon.CouponParam;
import com.shop.cereshop.app.service.activity.CereBuyerCouponService;
import com.shop.cereshop.app.service.coupon.CereCommonCouponService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.channel.ShopChannelActivity;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CereCommonCouponServiceImpl implements CereCommonCouponService {

    @Autowired
    private CereCommonCouponDAO cereCommonCouponDAO;

    @Autowired
    private CereBuyerCouponDAO cereBuyerCouponDAO;

    @Autowired
    private CereBuyerShopCouponDAO cereBuyerShopCouponDAO;

    @Autowired
    private ShopChannelActivityDAO shopChannelActivityDAO;

    @Autowired
    private ShopChannelActivityCouponDAO shopChannelActivityCouponDAO;

    @Autowired
    private CereBuyerCouponService cereBuyerCouponService;

    @Override
    public Page<CommonCoupon> getCouponCenterList(PageParam param, Long buyerUserId) {
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<CommonCoupon> list = cereCommonCouponDAO.selectCouponList();
        assembleState(buyerUserId, list);
        PageInfo<CommonCoupon> pageInfo = new PageInfo<>(list);
        return new Page<>(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    public List<CommonCoupon> getChannelActivityCoupon(ActivityParam param, Long buyerUserId) {
        List<CommonCoupon> list = cereCommonCouponDAO.getChannelActivityCoupon(param.getActivityId());
        assembleState(buyerUserId, list);
        return list;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void takeChannelActivityCoupon(Long shopChannelActivityId, CereBuyerUser user) throws CoBusinessException {
        ShopChannelActivity activity = shopChannelActivityDAO.selectById(shopChannelActivityId);
        if (activity == null) {
            throw new CoBusinessException(CoReturnFormat.ACTIVITY_NOT_EXISTS);
        }
        if (!IntegerEnum.TOOL_HAND.getCode().equals(activity.getState())) {
            throw new CoBusinessException(CoReturnFormat.ACTIVITY_STATE_ERROR);
        }
        if (activity.getRemainCount() < 1) {
            throw new CoBusinessException(CoReturnFormat.COUPON_RECEIVE_FINISH);
        }
        activity.setRemainCount(activity.getRemainCount() - 1);
        int update = shopChannelActivityDAO.updateCheckRemainCount(shopChannelActivityId);
        if (update < 1) {
            throw new CoBusinessException(CoReturnFormat.COUPON_RECEIVE_FINISH);
        }
        List<CommonCoupon> list = cereCommonCouponDAO.getChannelActivityCoupon(shopChannelActivityId);
        if (CollectionUtils.isNotEmpty(list)) {
            List<CouponParam> paramList = list.stream().map(obj -> {
                CouponParam param = new CouponParam();
                param.setSource(IntegerEnum.COUPON_SOURCE_CHANNEL_ACTIVITY.getCode());
                param.setShopCouponId(obj.getCouponId());
                return param;
            }).collect(Collectors.toList());
            cereBuyerCouponService.takeBatchCoupon(paramList, user);
        } else {
            throw new CoBusinessException(CoReturnFormat.CHANNEL_COUPON_SOLD_OUT_OR_END);
        }
    }

    private void assembleState(Long buyerUserId, List<CommonCoupon> list) {
        if (CollectionUtils.isNotEmpty(list) && buyerUserId != null) {
            for (CommonCoupon coupon:list) {
                int userTakeCount = 0;
                if (coupon.getSource() == 1) {
                    userTakeCount = cereBuyerCouponDAO.findCount(buyerUserId, coupon.getCouponId());
                } else {
                    userTakeCount = cereBuyerShopCouponDAO.findCount(buyerUserId, coupon.getCouponId());
                }
                coupon.setUserTakeCount(userTakeCount);
                if (userTakeCount > 0) {
                    coupon.setState(0);
                }
            }
        }
    }
}
