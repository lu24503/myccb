/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.coupon;

import com.shop.cereshop.app.param.coupon.ChannelCouponDetail;
import com.shop.cereshop.commons.domain.tool.CereChannelCoupon;

/**
 * <p>
 * 业务接口
 * 渠道券表
 * </p>
 *
 * @date 2021-12-12
 */
public interface CereChannelCouponService {

    /**
     * 查询渠道落地页
     * @param param
     * @return
     */
    ChannelCouponDetail getChannelCouponDetail(CereChannelCoupon param, Long buyerUserId);

}
