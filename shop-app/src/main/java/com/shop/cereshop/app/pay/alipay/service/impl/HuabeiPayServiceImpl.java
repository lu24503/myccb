/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.pay.alipay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.AlipayTradeRefundApplyModel;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.shop.cereshop.app.pay.alipay.service.HuabeiPayService;
import com.shop.cereshop.commons.config.AlipayConfig;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.WxPayEnum;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.RandomStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service(value = "huabeiPayService")
@Slf4j(topic = "HuabeiPayServiceImpl")
public class HuabeiPayServiceImpl implements HuabeiPayService {

    @Override
    public Map<String, String> gotoPay(String orderFormId, BigDecimal money, String openId, String ip, Integer type, Integer huabeiPeriod) throws CoBusinessException, Exception {
        String outTradeNo = "";
        String subject = "通用下单";
        outTradeNo=orderFormId+"-"+ RandomStringUtil.getRandomCode(3,0)+"XCX";

        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.create.
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(AlipayConfig.APP_NOTIFY_URL);
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount(money.toString());
        model.setSubject(subject);
        model.setBuyerId(openId);
        ExtendParams extendParams = new ExtendParams();
        extendParams.setHbFqNum(huabeiPeriod.toString());
        extendParams.setHbFqSellerPercent(AlipayConfig.HUABEI_CHARGE_TYPE == 1 ? "100" : "0");
        model.setExtendParams(extendParams);

        request.setBizModel(model);
        Map<String,String> result = new HashMap<>();

        try {
            AlipayTradeCreateResponse response = client.execute(request);
            if (response.isSuccess()) {
                String trade_no = response.getTradeNo();// 获取返回的tradeNO。
                result.put("tradeNo", trade_no);
            } else {
                throw new CoBusinessException(CoReturnFormat.PAY_ORDER_ERROR);
            }
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
            throw new CoBusinessException(CoReturnFormat.PAY_ORDER_ERROR);
        }

        return result;
    }

    @Override
    public Map<String, String> refund(String transactionId, String outRefundNo, BigDecimal total, BigDecimal refund) throws CoBusinessException, Exception {
        return null;
    }

    @Override
    public Map<String, String> orderRefund(String transactionId, String outRefundNo, BigDecimal total, BigDecimal refund) throws CoBusinessException, Exception {
        Map<String, String> result = new HashMap<>();

        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setNotifyUrl(AlipayConfig.REFUND_NOTIFY_URL);
        AlipayTradeRefundApplyModel model = new AlipayTradeRefundApplyModel();
        model.setTradeNo(transactionId);
        model.setRefundAmount(refund.toString());
        request.setBizModel(model);

        AlipayTradeRefundResponse response = client.execute(request);
        if (response.isSuccess()) {
            //这里只是申请退款，兼容微信的返回
            result.put("return_msg", WxPayEnum.REFUND_SUCCESS.getCode());
            result.put("return_code", WxPayEnum.REFUND_OK.getCode());
        } else {
            log.error("alipay refund failed: subCode = {}, subMsg = {}", response.getSubCode(), response.getSubMsg());
            throw new CoBusinessException(CoReturnFormat.APPLY_REFUND_FAILED);
        }
        return result;
    }

    @Override
    public Map<String, String> refundBond(String transactionId, String outRefundNo, BigDecimal total, BigDecimal refund) throws CoBusinessException, Exception {
        return null;
    }

    @Override
    public String getCollectionCode(String orderFormid, BigDecimal money, String ip, String tradeType) throws CoBusinessException, Exception {
        return null;
    }

    @Override
    public String getOrderCollectionCode(String orderFormid, BigDecimal money, String ip, String tradeType) throws CoBusinessException, Exception {
        return null;
    }
}
