/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.pay.weixin.skd;

import com.shop.cereshop.commons.utils.RandomStringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.util.*;

/****************************************************
 *
 *
 *
 * @author majker
 * @date: 2019/3/7
 * @version 1.0
 **************************************************/
public class WXPayUtil {

   /**
    * XML格式字符串转换为Map
    *
    * @param strXML XML字符串
    * @return XML数据转换后的Map
    * @throws Exception
    */
   public static Map<String, String> xmlToMap(String strXML) throws Exception {
       try {
           Map<String, String> data = new HashMap<String, String>();
           DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
           DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
           InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
           org.w3c.dom.Document doc = documentBuilder.parse(stream);
           doc.getDocumentElement().normalize();
           NodeList nodeList = doc.getDocumentElement().getChildNodes();
           for (int idx = 0; idx < nodeList.getLength(); ++idx) {
               Node node = nodeList.item(idx);
               if (node.getNodeType() == Node.ELEMENT_NODE) {
                   org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                   data.put(element.getNodeName(), element.getTextContent());
               }
           }
           try {
               stream.close();
           } catch (Exception ex) {
               // do nothing
           }
           return data;
       } catch (Exception ex) {
           WXPayUtil.getLogger().warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
           throw ex;
       }

   }

   /**
    * 将Map转换为XML格式的字符串
    *
    * @param data Map类型数据
    * @return XML格式的字符串
    * @throws Exception
    */
   public static String mapToXml(Map<String, String> data) throws Exception {
       DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
       DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
       org.w3c.dom.Document document = documentBuilder.newDocument();
       org.w3c.dom.Element root = document.createElement("xml");
       document.appendChild(root);
       for (String key: data.keySet()) {
           String value = data.get(key);
           if (value == null) {
               value = "";
           }
           value = value.trim();
           org.w3c.dom.Element filed = document.createElement(key);
           filed.appendChild(document.createTextNode(value));
           root.appendChild(filed);
       }
       TransformerFactory tf = TransformerFactory.newInstance();
       Transformer transformer = tf.newTransformer();
       DOMSource source = new DOMSource(document);
       transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
       transformer.setOutputProperty(OutputKeys.INDENT, "yes");
       StringWriter writer = new StringWriter();
       StreamResult result = new StreamResult(writer);
       transformer.transform(source, result);
       String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
       try {
           writer.close();
       }
       catch (Exception ex) {
       }
       return output;
   }


   /**
    * 生成带有 sign 的 XML 格式字符串
    *
    * @param data Map类型数据
    * @param key API密钥
    * @return 含有sign字段的XML
    */
   public static String generateSignedXml(final Map<String, String> data, String key) throws Exception {
       return generateSignedXml(data, key, WXPayConstants.SignType.MD5);
   }

   /**
    * 生成带有 sign 的 XML 格式字符串
    *
    * @param data Map类型数据
    * @param key API密钥
    * @param signType 签名类型
    * @return 含有sign字段的XML
    */
   public static String generateSignedXml(final Map<String, String> data, String key, WXPayConstants.SignType signType) throws Exception {
       String sign = generateSignature(data, key, signType);
       data.put(WXPayConstants.FIELD_SIGN, sign);
       return mapToXml(data);
   }


   /**
    * 判断签名是否正确
    *
    * @param xmlStr XML格式数据
    * @param key API密钥
    * @return 签名是否正确
    * @throws Exception
    */
   public static boolean isSignatureValid(String xmlStr, String key) throws Exception {
       Map<String, String> data = xmlToMap(xmlStr);
       if (!data.containsKey(WXPayConstants.FIELD_SIGN) ) {
           return false;
       }
       String sign = data.get(WXPayConstants.FIELD_SIGN);
       return generateSignature(data, key).equals(sign);
   }

   /**
    * 判断签名是否正确，必须包含sign字段，否则返回false。使用MD5签名。
    *
    * @param data Map类型数据
    * @param key API密钥
    * @return 签名是否正确
    * @throws Exception
    */
   public static boolean isSignatureValid(Map<String, String> data, String key) throws Exception {
       return isSignatureValid(data, key, WXPayConstants.SignType.MD5);
   }

   /**
    * 判断签名是否正确，必须包含sign字段，否则返回false。
    *
    * @param data Map类型数据
    * @param key API密钥
    * @param signType 签名方式
    * @return 签名是否正确
    * @throws Exception
    */
   public static boolean isSignatureValid(Map<String, String> data, String key, WXPayConstants.SignType signType) throws Exception {
       if (!data.containsKey(WXPayConstants.FIELD_SIGN) ) {
           return false;
       }
       String sign = data.get(WXPayConstants.FIELD_SIGN);
       return generateSignature(data, key, signType).equals(sign);
   }

   /**
    * 生成签名
    *
    * @param data 待签名数据
    * @param key API密钥
    * @return 签名
    */
   public static String generateSignature(final Map<String, String> data, String key) throws Exception {
       return generateSignature(data, key, WXPayConstants.SignType.MD5);
   }

   /**
    * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
    *
    * @param data 待签名数据
    * @param key API密钥
    * @param signType 签名方式
    * @return 签名
    */
   public static String generateSignature(final Map<String, String> data, String key, WXPayConstants.SignType signType) throws Exception {
       Set<String> keySet = data.keySet();
       String[] keyArray = keySet.toArray(new String[keySet.size()]);
       Arrays.sort(keyArray);
       StringBuilder sb = new StringBuilder();
       for (String k : keyArray) {
           if (k.equals(WXPayConstants.FIELD_SIGN)) {
               continue;
           }
           if(data.get(k) != null && data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
               sb.append(k).append("=").append(data.get(k).trim()).append("&");
       }
       sb.append("key=").append(key);
       if (WXPayConstants.SignType.MD5.equals(signType)) {
           return MD5(sb.toString()).toUpperCase();
       }
       else if (WXPayConstants.SignType.HMACSHA256.equals(signType)) {
           return HMACSHA256(sb.toString(), key);
       }
       else {
           throw new Exception(String.format("Invalid sign_type: %s", signType));
       }
   }

    /**
    * 获取随机字符串 Nonce Str
    *
    * @return String 随机字符串
    */
   public static String generateNonceStr() {
       return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
   }


   /**
    * 生成 MD5
    *
    * @param data 待处理数据
    * @return MD5结果
    */
   public static String MD5(String data) throws Exception {
       MessageDigest md = MessageDigest.getInstance("MD5");
       byte[] array = md.digest(data.getBytes("UTF-8"));
       StringBuilder sb = new StringBuilder();
       for (byte item : array) {
           sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
       }
       return sb.toString().toUpperCase();
   }

   /**
    * 生成 HMACSHA256
    * @param data 待处理数据
    * @param key 密钥
    * @return 加密结果
    * @throws Exception
    */
   public static String HMACSHA256(String data, String key) throws Exception {
       Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
       SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
       sha256_HMAC.init(secret_key);
       byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
       StringBuilder sb = new StringBuilder();
       for (byte item : array) {
           sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
       }
       return sb.toString().toUpperCase();
   }

   /**
    * 日志
    * @return
    */
   public static Logger getLogger() {
       Logger logger = LoggerFactory.getLogger("wxpay java sdk");
       return logger;
   }

   /**
    * 获取当前时间戳，单位秒
    * @return
    */
   public static long getCurrentTimestamp() {
       return System.currentTimeMillis()/1000;
   }

   /**
    * 获取当前时间戳，单位毫秒
    * @return
    */
   public static long getCurrentTimestampMs() {
       return System.currentTimeMillis();
   }

   /**
    * 生成 uuid， 即用来标识一笔单，也用做 nonce_str
    * @return
    */
   public static String generateUUID() {
       return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
   }

    /**
     * 申请退款
     */
    public static String doRefund(String url, String data,String cert_url,String mch_id) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //本地获取
//        File file = ResourceUtils.getFile(cert_url);
        //部署jar包获取;
        InputStream is = new FileInputStream(new File(cert_url));
        //WXPayUtil.class.getClassLoader().getResourceAsStream(cert_url);
        try {
            keyStore.load(is, mch_id.toCharArray());
        } finally {
            if (is != null) {
                is.close();
            }
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(
                keyStore,
                mch_id.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                null,
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER
        );
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息
//             httpost.addHeader("Connection", "keep-alive");
//             httpost.addHeader("Accept", "*/*");
//             httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//             httpost.addHeader("Host", "api.mch.wechat.qq.com");
//             httpost.addHeader("X-Requested-With", "XMLHttpRequest");
//             httpost.addHeader("Cache-Control", "max-age=0");
//             httpost.addHeader("ElectronicUser-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    public static Map<String, String> prepareParam(String mch_id, String appId,
                                    String orderFormId, BigDecimal money, String ip,
                                    String app_notify_url, String openid, String key,
                                    String typeFlag, String tradeType) throws Exception {
        Map<String, String> reqParams = new HashMap<>();
        //appId
        reqParams.put("appid", appId);
        //微信支付分配的商户号
        reqParams.put("mch_id", mch_id);
        //随机字符串
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "充值"+orderFormId+"订单-" + typeFlag);
        //订单总金额，单位为分
        reqParams.put("total_fee", money.multiply(BigDecimal.valueOf(100)).intValue() + "");
        //终端IP
        reqParams.put("spbill_create_ip", ip);
        //通知地址
        reqParams.put("notify_url", app_notify_url);
        //用户标识
        reqParams.put("openid", openid);

        String outTradeNo = orderFormId+"-"+ RandomStringUtil.getRandomCode(3,0)+typeFlag;
        //商户订单编号
        reqParams.put("out_trade_no", outTradeNo);
        //交易类型
        reqParams.put("trade_type", tradeType);

        //签名
        String sign = generateSignature(reqParams, key);
        reqParams.put("sign", sign);
        return reqParams;
    }

    public static Map<String, String> assembleResult(String xmlResult,
                                                     String nonceStr,
                                                     String appid,
                                                     String mch_id,
                                                     String key) throws Exception {
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        //预付单信息
        String prepay_id = result.get("prepay_id");
        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appId", appid);
        packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
        packageParams.put("nonceStr", nonceStr);
        packageParams.put("package", "prepay_id=" + prepay_id);
        packageParams.put("signType", "MD5");
        String packageSign = WXPayUtil.generateSignature(packageParams, key);
        packageParams.put("prepayId", prepay_id);
        packageParams.put("paySign", packageSign);
        packageParams.put("codeUrl", result.get("code_url"));
        packageParams.put("partnerId", mch_id);
        packageParams.put("mweb_url", result.get("mweb_url"));
        return packageParams;
    }
}

