/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.pay.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shop.cereshop.app.pay.PayFactory;
import com.shop.cereshop.app.pay.weixin.service.WxPayService;
import com.shop.cereshop.app.pay.weixin.skd.PaymentApi;
import com.shop.cereshop.app.pay.weixin.skd.PaymentKit;
import com.shop.cereshop.app.pay.weixin.skd.WXPayUtil;
import com.shop.cereshop.app.pay.weixin.skd.WXPayV3Util;
import com.shop.cereshop.app.pay.weixin.strategy.WxPayStrategy;
import com.shop.cereshop.commons.constant.WxPayEnum;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.RandomStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service(value  = "wxPayService")
@Slf4j(topic = "WxPayServiceImpl")
public class WxPayServiceImpl implements WxPayService {

    /**
     * 支付小程序appid
     */
    @Value("${weixin.appid}")
    private String appid;

    /**
     * 支付APP端的appid
     */
    @Value("${weixin.app_appid}")
    private String app_appid;

    /**
     * 支付小程序秘钥
     */
    @Value("${weixin.secret}")
    private String secret;

    /**
     * 商户号
     */
    @Value("${weixin.mchid}")
    private String mch_id;

    /**
     * 证书路径
     */
    @Value("${weixin.certurl}")
    private String cert_url;

    /**
     * pc回调地址
     */
    @Value("${weixin.pc_notifyurl}")
    private String pc_notify_url;

    /**
     * pc退款回调地址
     */
    @Value("${weixin.pc_refund_notifyurl}")
    private String pc_refund_notify_url;

    /**
     * pc退款回调地址
     */
    @Value("${weixin.order_refund_notifyurl}")
    private String order_refund_notifyurl;

    /**
     * pc保证金退款回调地址
     */
    @Value("${weixin.pc_bond_refund_notifyurl}")
    private String pc_bond_refund_notify_url;

    /**
     * app回调地址
     */
    @Value("${weixin.app_notifyurl}")
    private String app_notify_url;

    @Value("${weixin.app_v3_notifyurl}")
    private String appV3NotifyUrl;

    /**
     * 商户秘钥
     */
    @Value("${weixin.key}")
    private String key;

    /**
     * h5支付跳转地址
     */
    @Value("${weixin.redirect_url}")
    private String redirectUrl;

    @Autowired
    private WXPayV3Util wxPayV3Util;

    @Override
    public Map<String, String> gotoPay(String orderFormId, BigDecimal money, String openid, String ip,
                                       Integer type, Integer huabeiPeriod) throws CoBusinessException, Exception {
        WxPayStrategy wxPayStrategy = PayFactory.getWxPayStrategy(type);
        return wxPayStrategy.prepay(appid, app_appid, openid,
                mch_id, app_notify_url, redirectUrl, ip, money,
                orderFormId, key);
    }

    @Override
    public Map<String, String> refund(String transactionId, String outRefundNo, BigDecimal total, BigDecimal refund) throws CoBusinessException, Exception {
        //退款资金来源-可用余额退款
        String refundAccount="REFUND_SOURCE_RECHARGE_FUNDS";
        Map<String, String> params = new HashMap<>();
        if(outRefundNo.contains("APP")){
            //APP微信退款
            params.put("appid", app_appid);
        } else {
            params.put("appid", appid);
        }
        params.put("mch_id",mch_id);
        params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //商户订单号和微信订单号二选一
//        params.put("out_trade_no", wxPayLog.getOutTradeNo());
        params.put("transaction_id", transactionId);
        params.put("out_refund_no", outRefundNo);
        params.put("total_fee", total.multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("refund_fee", refund.multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("refund_account", refundAccount);
        // 退款原因，若商户传入，会在下发给用户的退款消息中体现退款原因
        params.put("refund_desc","退款");
        //退款回调
        params.put("notify_url", pc_refund_notify_url);
        //签名算法
        String sign = WXPayUtil.generateSignature(params,key);
        params.put("sign", sign);
        String xml = PaymentKit.toXml(params);
        log.info(xml);
        String xmlStr = WXPayUtil.doRefund("https://api.mch.weixin.qq.com/secapi/pay/refund", xml,cert_url,mch_id);
        log.info(xmlStr);
        //加入微信支付日志
        Map map = PaymentKit.xmlToMap(xmlStr);
        return map;
    }

    @Override
    public Map<String, String> orderRefund(String transactionId, String outRefundNo, BigDecimal total, BigDecimal refund) throws Exception {
        //APPV3版本的退款，以APPV3开头
        if (outRefundNo.startsWith("APPV3")) {
            return refundWithV3(transactionId, outRefundNo, total, refund);
        } else {
            return refundWithNormal(transactionId, outRefundNo, total, refund);
        }
    }

    private Map<String, String> refundWithV3(String transactionId, String outRefundNo,
                                             BigDecimal total, BigDecimal refund) throws Exception {
        log.info(" orderRefund refundWithV3 {}, {}, {}, {}", transactionId, outRefundNo,
                total, refund);
        Map<String, Object> params = new HashMap<>();
        params.put("transaction_id", transactionId);
        params.put("out_refund_no", outRefundNo);
        params.put("notify_url", appV3NotifyUrl);
        params.put("amount", new HashMap<String, Object>(){
            {
                put("refund", refund.multiply(new BigDecimal(100)).intValue());
                put("total", total.multiply(new BigDecimal(100)).intValue());
                put("currency", "CNY");
            }
        });
        JSONObject obj = wxPayV3Util.doPostWeixinV3(WXPayV3Util.api_v3_refund_url, JSON.toJSONString(params));
        log.info(" orderRefund refundWithV3 result {}", JSON.toJSONString(obj));
        Map<String, String> result = new HashMap<>();
        if (obj.getString("status") != null
                && (WxPayEnum.REFUND_OK.getCode().equals(obj.getString("status"))
                || WxPayEnum.REFUND_PROCESSING.getCode().equals(obj.getString("status")))) {
            result.put("return_code", WxPayEnum.REFUND_OK.getCode());
            result.put("return_msg", WxPayEnum.REFUND_SUCCESS.getCode());
        } else if (WxPayEnum.NOT_ENOUGH_V3.getCode().equals(obj.getString("status"))) {
            result.put("return_code", WxPayEnum.BUSINESS_BALANCE_NOTENOUGH.getCode());
        }
        return result;
    }

    private Map<String, String> refundWithNormal(String transactionId, String outRefundNo,
                                 BigDecimal total, BigDecimal refund) throws Exception {
        //退款资金来源-可用余额退款
        String refundAccount="REFUND_SOURCE_RECHARGE_FUNDS";
        Map<String, String> params = new HashMap<>();
        if(outRefundNo.contains("APP")){
            //APP微信退款
            params.put("appid", app_appid);
        } else {
            params.put("appid", appid);
        }
        params.put("mch_id",mch_id);
        params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //商户订单号和微信订单号二选一
//        params.put("out_trade_no", wxPayLog.getOutTradeNo());
        params.put("transaction_id", transactionId);
        params.put("out_refund_no", outRefundNo);
        params.put("total_fee", total.multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("refund_fee", refund.multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("refund_account", refundAccount);
        // 退款原因，若商户传入，会在下发给用户的退款消息中体现退款原因
        params.put("refund_desc","退款");
        //退款回调
        params.put("notify_url", order_refund_notifyurl);
        //签名算法
        String sign = WXPayUtil.generateSignature(params,key);
        params.put("sign", sign);
        String xml = PaymentKit.toXml(params);
        log.info(xml);
        String xmlStr = WXPayUtil.doRefund("https://api.mch.weixin.qq.com/secapi/pay/refund", xml, cert_url, mch_id);
        log.info(xmlStr);
        //加入微信支付日志
        return PaymentKit.xmlToMap(xmlStr);
    }

    @Override
    public Map<String, String> refundBond(String transactionId, String outRefundNo, BigDecimal total, BigDecimal refund) throws CoBusinessException, Exception {
        //退款资金来源-可用余额退款
        String refundAccount="REFUND_SOURCE_RECHARGE_FUNDS";
        Map<String, String> params = new HashMap<>();
        if(outRefundNo.contains("APP")){
            //APP微信退款
            params.put("appid", app_appid);
        } else {
            params.put("appid", appid);
        }
        params.put("mch_id",mch_id);
        params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //商户订单号和微信订单号二选一
//        params.put("out_trade_no", wxPayLog.getOutTradeNo());
        params.put("transaction_id", transactionId);
        params.put("out_refund_no", outRefundNo);
        params.put("total_fee", total.multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("refund_fee", refund.multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("refund_account", refundAccount);
        // 退款原因，若商户传入，会在下发给用户的退款消息中体现退款原因
        params.put("refund_desc","退款");
        //退款回调
        params.put("notify_url", pc_bond_refund_notify_url);
        //签名算法
        String sign = WXPayUtil.generateSignature(params,key);
        params.put("sign", sign);
        String xml = PaymentKit.toXml(params);
        log.info(xml);
        String xmlStr = WXPayUtil.doRefund("https://api.mch.weixin.qq.com/secapi/pay/refund", xml,cert_url,mch_id);
        log.info(xmlStr);
        //加入微信支付日志
        Map map = PaymentKit.xmlToMap(xmlStr);
        return map;
    }

    @Override
    public String getCollectionCode(String orderFormid, BigDecimal money, String ip, String tradeType) throws CoBusinessException,Exception {
        Map<String, String> reqParams = new HashMap<>();
        //生成一个新的订单支付编号
        String outTradeNo=orderFormid+"-"+ RandomStringUtil.getRandomCode(3,0);
        //微信分配的小程序ID
        reqParams.put("appid",appid);
        //微信支付分配的商户号
        reqParams.put("mch_id",mch_id);
        //随机字符串
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "充值"+orderFormid+"订单-微信小程序");

        //商户订单编号
        reqParams.put("out_trade_no", outTradeNo);
        //订单总金额，单位为分
        reqParams.put("total_fee", money.multiply(BigDecimal.valueOf(100)).intValue() + "");
        //终端IP
//        reqParams.put("spbill_create_ip", "127.0.0.1");
        reqParams.put("spbill_create_ip", ip);
        //支付回调地址
        reqParams.put("notify_url", pc_notify_url);
        //交易类型
        reqParams.put("trade_type", tradeType);
        //用户标识
//        reqParams.put("openid", openid);
        //签名
        String sign = WXPayUtil.generateSignature(reqParams,key);
        reqParams.put("sign", sign);
        /*
            调用支付定义下单API,返回预付单信息 prepay_id
         */
        String xmlResult = PaymentApi.pushOrder(reqParams);
        log.info(xmlResult);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        String code_url = result.get("code_url");
        return code_url;
    }

    @Override
    public String getOrderCollectionCode(String orderFormid, BigDecimal money, String ip, String tradeType) throws CoBusinessException,Exception {
        Map<String, String> reqParams = new HashMap<>();
        //生成一个新的订单支付编号
        String outTradeNo=orderFormid+"-"+ RandomStringUtil.getRandomCode(3,0);
        //微信分配的小程序ID
        reqParams.put("appid",appid);
        //微信支付分配的商户号
        reqParams.put("mch_id",mch_id);
        //随机字符串
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "充值"+orderFormid+"订单-微信小程序");

        //商户订单编号
        reqParams.put("out_trade_no", outTradeNo);
        //订单总金额，单位为分
        reqParams.put("total_fee", money.multiply(BigDecimal.valueOf(100)).intValue() + "");
        //终端IP
//        reqParams.put("spbill_create_ip", "127.0.0.1");
        reqParams.put("spbill_create_ip", ip);
        //支付回调地址
        reqParams.put("notify_url", app_notify_url);
        //交易类型
        reqParams.put("trade_type", tradeType);
        //用户标识
//        reqParams.put("openid", openid);
        //签名
        String sign = WXPayUtil.generateSignature(reqParams,key);
        reqParams.put("sign", sign);
        /*
            调用支付定义下单API,返回预付单信息 prepay_id
         */
        String xmlResult = PaymentApi.pushOrder(reqParams);
        log.info(xmlResult);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        String code_url = result.get("code_url");
        return code_url;
    }
}
