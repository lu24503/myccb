/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.pay.alipay.service.impl;

import com.alipay.api.*;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.AlipayTradeRefundApplyModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.shop.cereshop.app.pay.alipay.service.AliPayService;
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
import java.util.UUID;

@Service(value = "aliPayService")
@Slf4j(topic = "AliPayServiceImpl")
public class AliPayServiceImpl implements AliPayService {

    /**
     * 支付小程序appid

    @Value("${alipay.appid}")
    private String appid;
     */
    public static void main(String[] args) throws AlipayApiException {
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = UUID.randomUUID().toString().substring(0,32);
        // 订单名称，必填
        String subject = "alipayOrder";
        System.out.println(subject);
        // 付款金额，必填
        String total_amount="1";
        // 商品描述，可空
        String body = "";
        // 超时时间 可空
        String timeout_express="2m";
        // 销售产品码 必填
        String product_code="QUICK_WAP_WAY";
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();

        //设置网关地址
        certAlipayRequest.setServerUrl(AlipayConfig.URL);
        //设置应用Id
        certAlipayRequest.setAppId(AlipayConfig.APPID);
        //设置应用私钥
        certAlipayRequest.setPrivateKey(AlipayConfig.RSA_PRIVATE_KEY);
        //设置请求格式，固定值json
        certAlipayRequest.setFormat(AlipayConfig.FORMAT);
        //设置字符集
        certAlipayRequest.setCharset(AlipayConfig.CHARSET);
        //设置签名类型
        certAlipayRequest.setSignType(AlipayConfig.SIGNTYPE);
        //设置应用公钥证书路径
        //certAlipayRequest.setCertPath(AlipayConfig.APP_CERT_PATH);
        //设置支付宝公钥证书路径
        //certAlipayRequest.setAlipayPublicCertPath(AlipayConfig.ALIPAY_CERT_PATH);
        //设置支付宝根证书路径
        //certAlipayRequest.setRootCertPath(AlipayConfig.ALIPAY_ROOT_CERT_PATH);

        //AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayClient client = new DefaultAlipayClient(certAlipayRequest);
        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        //model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        // alipay_request.setNotifyUrl(AlipayConfig.APP_NOTIFY_URL);
        // 设置同步地址
        // alipay_request.setReturnUrl(AlipayConfig.REDIRECT_URL);
        alipay_request.setNeedEncrypt(false);

        try {
            // 调用SDK生成表单
            AlipayTradeWapPayResponse response = client.certificateExecute(alipay_request);
            //AlipayTradeWapPayResponse response = client.execute(alipay_request);
            if (response.isSuccess()) {
                log.info("code:" + response.getCode());
                log.info("msg:" + response.getMsg());
                log.info("subCode:" + response.getSubCode());
                log.info("subMsg:" + response.getSubMsg());
                log.info("body:" + response.getBody());
                log.info("params:" + response.getParams());
            } else {
                log.error("pay failed: " + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

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
        /*request.setBizContent("{" +
                "\"out_trade_no\":\"" + outTradeNo + "\"," +// 可以随机生成订单号：String outTradeNo = UUID.randomUUID().toString().replace("-", "");
                "\"total_amount\":" + money.toString() + "," +
                "\"extend_params\":" + money.toString() + "," +
                "\"subject\":\"" + subject +"\"," +
                "\"buyer_id\":\"" + openId + "\"" + // 小程序支付场景中该参数必传
                "}");*/
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount(money.toString());
        model.setSubject(subject);
        model.setBuyerId(openId);

        request.setBizModel(model);
        Map<String,String> result = new HashMap<>();

        try {
            AlipayTradeCreateResponse response = client.execute(request);
            String trade_no = response.getTradeNo();// 获取返回的tradeNO。
            result.put("tradeNo", trade_no);
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
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
