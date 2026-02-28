package com.shop.cereshop.app.service.coupon;

import com.shop.cereshop.app.page.coupon.CommonCoupon;
import com.shop.cereshop.app.param.coupon.ActivityParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereCommonCouponService {

    /**
     * 查询领券中心
     * @param param
     * @param buyerUserId
     * @return
     */
    Page<CommonCoupon> getCouponCenterList(PageParam param, Long buyerUserId);

    /**
     * 查询渠道活动券列表
     * @param param
     * @param buyerUserId
     * @return
     */
    List<CommonCoupon> getChannelActivityCoupon(ActivityParam param, Long buyerUserId);

    /**
     * 一键领取渠道优惠券
     * @param shopChannelActivityId
     * @param user
     */
    void takeChannelActivityCoupon(Long shopChannelActivityId, CereBuyerUser user) throws CoBusinessException;
}
