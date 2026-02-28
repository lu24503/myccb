package com.shop.cereshop.app.pay.weixin.strategy.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shop.cereshop.app.pay.weixin.skd.WXPayV3Util;
import com.shop.cereshop.app.pay.weixin.strategy.WxPayStrategy;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.RandomStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service(value  = "appV3PayStrategyImpl")
@Slf4j(topic = "appV3PayStrategyImpl")
public class AppV3PayStrategyImpl implements WxPayStrategy {

    @Autowired
    private WXPayV3Util wxPayV3Util;

    @Value("${weixin.app_v3_notifyurl}")
    private String appV3NotifyUrl;

    @Override
    public Map<String, String> prepay(String appid, String app_appid, String openid,
                                      String mch_id, String app_notify_url, String redirectUrl,
                                      String ip, BigDecimal money, String orderFormId,
                                      String key) throws CoBusinessException {
        try {
            Map<String, Object> reqParams = new HashMap<>();
            reqParams.put("mchid", mch_id);
            String outTradeNo = orderFormId + "-" + RandomStringUtil.getRandomCode(3,0) + "APPV3";
            reqParams.put("out_trade_no", outTradeNo);
            reqParams.put("appid", app_appid);
            reqParams.put("description", "充值"+orderFormId+"订单-" + "APPV3");
            reqParams.put("notify_url", appV3NotifyUrl);
            reqParams.put("amount", new HashMap<String, Object>(){
                {
                    put("total", money.multiply(BigDecimal.valueOf(100)).intValue());
                    put("currency", "CNY");
                }
            });

            log.info("appV3PayStrategyImpl {}", JSON.toJSONString(reqParams));
            JSONObject result = wxPayV3Util.doPostWeixinV3(WXPayV3Util.api_v3_placeAnOrder_url, JSONUtil.toJsonStr(reqParams));
            log.info("appV3PayStrategyImpl {}", result.toJSONString());

            if(ObjectUtil.isNull(result.get("prepay_id"))){
                throw new CoBusinessException("微信支付下单失败，请检查配置");
            }

            String prepay_id = (String)result.get("prepay_id");
            // 获取app拉起支付签名
            String nonceStr = System.currentTimeMillis() / 1000 + "";
            long timestamp = System.currentTimeMillis() / 1000;
            String sign = null;
            try {
                sign = wxPayV3Util.sign(app_appid, timestamp, nonceStr, prepay_id);
            } catch (Exception e) {
                log.error(" sign fail AppV3PayStrategyImpl {}", e.getMessage(), e);
                throw new CoBusinessException("生成签名错误："+e.getMessage());
            }
            log.info("sign____________________"+sign);
            Map<String,String> returnMap = new HashMap<>();
            returnMap.put("appId",app_appid);
            returnMap.put("partnerId",mch_id);
            returnMap.put("prepayId",prepay_id);
            returnMap.put("packages","Sign=WXPay");
            returnMap.put("nonceStr",nonceStr);
            returnMap.put("timeStamp", String.valueOf(timestamp));
            returnMap.put("paySign",sign);
            log.info("returnMap________"+returnMap);
            return returnMap;
        } catch (Exception e) {
            log.error(" pay fail AppV3PayStrategyImpl {}", e.getMessage(), e);
            throw new CoBusinessException(CoReturnFormat.PAY_ORDER_ERROR);
        }
    }
}
