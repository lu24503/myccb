package com.shop.cereshop.app.pay.weixin.strategy;

import com.shop.cereshop.commons.exception.CoBusinessException;

import java.math.BigDecimal;
import java.util.Map;

public interface WxPayStrategy {

    Map<String, String> prepay(String appid, String app_appid, String openid,
                               String mch_id, String app_notify_url, String redirectUrl,
                               String ip, BigDecimal money, String orderFormId,
                               String key) throws CoBusinessException;

}
