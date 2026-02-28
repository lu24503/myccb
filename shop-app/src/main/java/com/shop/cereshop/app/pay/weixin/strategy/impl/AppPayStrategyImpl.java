package com.shop.cereshop.app.pay.weixin.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.shop.cereshop.app.pay.weixin.skd.PaymentApi;
import com.shop.cereshop.app.pay.weixin.skd.WXPayUtil;
import com.shop.cereshop.app.pay.weixin.strategy.WxPayStrategy;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.exception.CoBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service(value  = "appPayStrategyImpl")
@Slf4j(topic = "appPayStrategyImpl")
public class AppPayStrategyImpl implements WxPayStrategy {

    private static final String TYPE_FLAG = "APP";

    private static final String TRADE_TYPE = "APP";

    @Override
    public Map<String, String> prepay(String appid, String app_appid, String openid,
                                      String mch_id, String app_notify_url, String redirectUrl,
                                      String ip, BigDecimal money, String orderFormId,
                                      String key) throws CoBusinessException {
        try {
            Map<String, String> reqParams = WXPayUtil.prepareParam(mch_id, app_appid, orderFormId,
                    money, ip, app_notify_url, openid, key, TYPE_FLAG, TRADE_TYPE);

            //调用支付定义下单API,返回预付单信息 prepay_id
            log.info(JSON.toJSONString(reqParams));
            String xmlResult = PaymentApi.pushOrder(reqParams);
            log.info(xmlResult);

            return WXPayUtil.assembleResult(xmlResult, reqParams.get("nonce_str"), appid, mch_id, key);
        } catch (Exception e) {
            log.error(" pay fail AppPayStrategyImpl {}", e.getMessage(), e);
            throw new CoBusinessException(CoReturnFormat.PAY_ORDER_ERROR);
        }
    }
}
