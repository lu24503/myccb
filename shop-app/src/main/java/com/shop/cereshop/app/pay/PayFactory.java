/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.pay;

import com.shop.cereshop.app.pay.alipay.service.AliPayService;
import com.shop.cereshop.app.pay.alipay.service.HuabeiPayService;
import com.shop.cereshop.app.pay.ccb.service.CcbPayService;
import com.shop.cereshop.app.pay.weixin.service.WxPayService;
import com.shop.cereshop.app.pay.weixin.strategy.WxPayStrategy;
import com.shop.cereshop.app.pay.weixin.strategy.impl.AppPayStrategyImpl;
import com.shop.cereshop.app.pay.weixin.strategy.impl.AppV3PayStrategyImpl;
import com.shop.cereshop.app.pay.weixin.strategy.impl.H5PayStrategyImpl;
import com.shop.cereshop.app.pay.weixin.strategy.impl.XCXPayStrategyImpl;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.constant.ParamEnum;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.SpringUtil;

public class PayFactory {

    public static PayService getPayService(Integer paymentMode) throws CoBusinessException {
        if (paymentMode != null) {
            if (paymentMode.equals(IntegerEnum.ORDER_PAY_WX.getCode())) {
                return SpringUtil.getBean(WxPayService.class);
            } else if (paymentMode.equals(IntegerEnum.ORDER_PAY_ALI.getCode())) {
                return SpringUtil.getBean(AliPayService.class);
            } else if (paymentMode.equals(IntegerEnum.ORDER_PAY_HUABEI.getCode())) {
                return SpringUtil.getBean(HuabeiPayService.class);
            } else if (paymentMode.equals(IntegerEnum.ORDER_PAY_CCB.getCode())) {
                // 新增：路由到建行支付
                return SpringUtil.getBean(CcbPayService.class);
            }
        }
        throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
    }

    public static WxPayStrategy getWxPayStrategy(Integer payType) throws CoBusinessException {
        if (payType != null) {
            if(ParamEnum.PAY_XCX.getCode().equals(payType)){
                return SpringUtil.getBean(XCXPayStrategyImpl.class);
            } else if (ParamEnum.PAY_H5.getCode().equals(payType)) {
                return SpringUtil.getBean(H5PayStrategyImpl.class);
            } else if (ParamEnum.PAY_APP_V3.getCode().equals(payType)) {
                return SpringUtil.getBean(AppV3PayStrategyImpl.class);
            } else if (ParamEnum.PAY_APP.getCode().equals(payType)) {
                return SpringUtil.getBean(AppPayStrategyImpl.class);
            }
        }
        throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
    }

}
