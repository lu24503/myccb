package com.shop.cereshop.app.pay.ccb.service.impl;

import com.shop.cereshop.app.pay.ccb.service.CcbPayService;
import com.shop.cereshop.app.pay.ccb.utils.CcbLifeApiClient;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.ccb.MD5Util;
import com.shop.cereshop.commons.utils.ccb.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CcbPayServiceImpl implements CcbPayService {

    @Autowired
    private CcbLifeApiClient ccbLifeApiClient;

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
        params.put("ORDERID", orderFormid); // 传cereshop的订单号
        params.put("PAYMENT", money.toString());
        params.put("CURCODE", "01"); // 01表示人民币
        params.put("TXCODE", "520100"); // 具体的接口交易码
        params.put("REMARK1", "南京途悦电子商务");

        // 3. 将参数拼接为 key=value&key2=value2 格式用于签名和加密
        String paramStr = buildParamString(params);

        // 4. 生成MD5签名 (根据《建行相关APP服务方接入文档》服务方公钥参与MD5规则)
        // 使用您文档中提供的 MD5Util
        String mac = MD5Util.getMD5(paramStr + "&PUBKEY=" + "服务方公钥");

        // 5. RSA分段加密 (vparam)
        // 使用您文档中提供的 RSAUtil.encrypt 逻辑
        String vparam = RSAUtil.encrypt(paramStr, "建行公钥");

        // 6. 将加密参数返回给前端（前端将其传入 JS Bridge）
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("vparam", vparam);
        resultMap.put("vmac", mac);
        // 如果是建行小程序 API，可能还需要返回 platformId
        resultMap.put("platformId", "您的建行服务方编号");

        return resultMap;
    }

    private String buildParamString(Map<String, String> params) {
        // 实现参数按照 ASCII 排序并拼接为字符串...
        return "";
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
