package com.shop.cereshop.app.pay.ccb.service.impl;

import com.shop.cereshop.app.pay.ccb.service.CcbPayService;
import com.shop.cereshop.app.pay.ccb.utils.CcbLifeApiClient;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.ccb.MD5Util;
import com.shop.cereshop.commons.utils.ccb.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CcbPayServiceImpl implements CcbPayService {

    @Autowired
    private CcbLifeApiClient ccbLifeApiClient;

    @Value("${ccb.life.key.sp-public}")
    private String spPublic; // 服务方公钥

    @Override
    public Map<String, String> gotoPay(String orderFormid, BigDecimal money, String openid, String ip, Integer type, Integer huabeiPeriod) throws CoBusinessException, Exception {

        // 1. 【新增】前置操作：将订单推送到建行生活
        // 注：此处可根据需要查询数据库，获取更详细的商品标题等信息传入
        String orderTitle = "南京途悦电子商城订单-" + orderFormid;
        boolean isPushed = ccbLifeApiClient.pushOrder(orderFormid, money, orderTitle, openid);

        if (!isPushed) {
            throw new CoBusinessException("建行生活订单推送失败，无法拉起支付");
        }

        // 2. 组装建行支付需要的基础参数（严格按照《建行生活输入通讯报文》规范）
        Map<String, String> params = new HashMap<>();
        params.put("MERCHANTID", "从配置获取商户代码");
        params.put("POSID", "从配置获取柜台代码");
        params.put("BRANCHID", "分行代码");
        /**
         * 通常情况下，若服务方对订单号没有特殊要求，ORDERID与USER_ORDERID可以保持一致；
         * 考虑到存在一笔订单用户会多次发起支付的情况，若服务方对同一笔订单每次发起支付的流水号要求不一样，可以使用同一USER_ORDERID（订单号）对应不同ORDERID（流水号）来区分每笔支付交易。
         */
        params.put("ORDERID", orderFormid); //商户发起支付的流水号，用于建行收单支付流程，包括在支付、查询支付结果及退款交易中使用。
        params.put("USER_ORDERID", orderFormid); //用户订单号，用于同步用户在建行生活中的订单状态，包括在订单推送及状态更新时使用。

        params.put("PAYMENT", money.toString());
        params.put("CURCODE", "01"); // 01表示人民币
        params.put("TXCODE", "520100"); // 具体的接口交易码
        params.put("REMARK1", "");
        params.put("REMARK2", "上送YS开头的服务方编号，与PLATFORMID保持一致");
        params.put("TYPE","1");//接口类型，默认送 1（防钓鱼接口）
        params.put("GATEWAY","0");//网关类型，默认送0
        params.put("CLIENTIP","");//客户端IP，送空即可
        params.put("REGINFO","");//客户注册信息，客户在商户系统中注册的信息，中文需使用escape编码。送空值即可
        //@TODO 商品名称中文需编码
        params.put("PROINFO","");//商品信息，客户购买的商品信息，收银台会展示该信息，中文需使用escape编码。建议编码前长度不超过50位
        params.put("REFERER","");//商户URL，送空即可
        params.put("THIRDAPPINFO","comccbpay1234567890cloudmerchant");//客户端标识，通过建行相关App下单场景，订单中客户端标识固定设为comccbpay1234567890cloudmerchant
        String timeoutStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        params.put("TIMEOUT",timeoutStr);//订单超时时间,格式：YYYYMMDDHHMMSS（如：20120214143005）银行系统时间> TIMEOUT时拒绝交易，若送空值则不判断超时。当该字段有值时参与MAC校验，否则不参与MAC校验。
//        params.put("DCEP_MCT_TYPE","1");//数币商户类型,默认为空，特定场景使用。0\空-不识别为数币商户;1-融合商户;2-非融合商户
        // 3. 将参数按顺序拼接用于签名和加密
        String paramStr = buildParamString(params);
        // 4. 生成MD5签名 (根据《建行相关APP服务方接入文档》服务方公钥参与MD5规则)
        // 使用您文档中提供的 MD5Util
        String mac = MD5Util.getMD5(paramStr + "&PLATFORMPUB=" + spPublic);//参数拼接上服务方公钥进行MD5计算，得到mac

        // 5. RSA分段加密 (vparam)
        // 使用您文档中提供的 RSAUtil.encrypt 逻辑
        String vparam = RSAUtil.encrypt(paramStr, "建行公钥");

        // 6. 将加密参数返回给前端（前端将其传入 JS Bridge）
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("vparam", vparam);
        resultMap.put("vmac", mac);
        // 如果是建行小程序 API，可能还需要返回 platformId
        resultMap.put("platformId", "建行服务方编号");

        return resultMap;
    }

    private String buildParamString(Map<String, String> params) {
        String result = params.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        return result;
    }

    @Override
    public Map<String, String> refund(String transactionId, String outRefundNo, BigDecimal total, BigDecimal refund) throws CoBusinessException, Exception {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> orderRefund(String transactionId, String outRefundNo, BigDecimal total, BigDecimal refund) throws CoBusinessException, Exception {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> refundBond(String transactionId, String outRefundNo, BigDecimal total, BigDecimal refund) throws CoBusinessException, Exception {
        return Collections.emptyMap();
    }

    @Override
    public String getCollectionCode(String orderFormid, BigDecimal money, String ip, String tradeType) throws CoBusinessException, Exception {
        return "";
    }

    @Override
    public String getOrderCollectionCode(String orderFormid, BigDecimal money, String ip, String tradeType) throws CoBusinessException, Exception {
        return "";
    }
}
