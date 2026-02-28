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
import java.net.URLEncoder;
import java.util.Map;

@Service(value  = "h5PayStrategyImpl")
@Slf4j(topic = "h5PayStrategyImpl")
public class H5PayStrategyImpl implements WxPayStrategy {

    private static final String TYPE_FLAG = "H5";

    private static final String TRADE_TYPE = "MWEB";

    @Override
    public Map<String, String> prepay(String appid, String app_appid, String openid,
                                      String mch_id, String app_notify_url, String redirectUrl,
                                      String ip, BigDecimal money, String orderFormId,
                                      String key) throws CoBusinessException {
        try {
            Map<String, String> reqParams = WXPayUtil.prepareParam(mch_id, appid, orderFormId,
                    money, ip, app_notify_url, openid, key, TYPE_FLAG, TRADE_TYPE);

            //调用支付定义下单API,返回预付单信息 prepay_id
            log.info("h5PayStrategyImpl {}", JSON.toJSONString(reqParams));
            String xmlResult = PaymentApi.pushOrder(reqParams);
            log.info("h5PayStrategyImpl {}", xmlResult);

            Map<String, String> packageParams = WXPayUtil.assembleResult(xmlResult, reqParams.get("nonce_str"), appid, mch_id, key);
            packageParams.put("mwebUrl", packageParams.get("mweb_url") + "&redirect_url="+ URLEncoder.encode(redirectUrl, "GBK"));
            return packageParams;
        } catch (Exception e) {
            log.error(" pay fail H5PayStrategyImpl {}", e.getMessage(), e);
            throw new CoBusinessException(CoReturnFormat.PAY_ORDER_ERROR);
        }
    }
}
