package com.shop.cereshop.app.pay.ccb.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shop.cereshop.commons.exception.CoBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 建行生活服务端通讯客户端
 */
@Slf4j
@Component
public class CcbLifeApiClient {

    @Value("${ccb.life.api.url}")
    private String apiUrl; // 建行生活服务端网关地址，例如 http://128.192.179.60/uat_new/tp_service/txCtrl/server

    @Value("${ccb.life.api.channel}")
    private String txChnl; // 通讯渠道号，如 YS44...

    @Value("${ccb.life.merchant.id}")
    private String merchantId; // 商户号

    /**
     * 推送订单到建行生活 (接口: A3341O031)
     */
    public boolean pushOrder(String orderId, BigDecimal amount, String title, String buyerId) throws CoBusinessException {
        // 1. 组装公共报文头 (CLD_HEADER)
        Map<String, Object> header = new HashMap<>();
        header.put("CLD_TX_CHNL", txChnl);
        header.put("CLD_TX_TIME", DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        header.put("CLD_TX_CODE", "A3341O031");
        header.put("CLD_TX_SEQ", IdUtil.simpleUUID()); // 全局事件流水号，保证唯一

        // 2. 组装请求体 (根据A3341O031规范填充业务字段)
        Map<String, Object> body = new HashMap<>();
        body.put("MERCHANTID", merchantId);
        body.put("ORDERID", orderId);
        body.put("PAYMENT", amount.toString());
        body.put("ORDER_TITLE", title); // 订单标题或商品名称
        body.put("USERID", buyerId); // 建行会员编号或业务用户ID
        // 注意：这里需要根据具体的《建行生活输入通讯报文》补全 A3341O031 所要求的其他必填业务字段，如门店ID、商品明细等

        // 3. 组合完整报文
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("CLD_HEADER", header);
        requestMap.put("CLD_BODY", body); // 视建行具体 JSON 结构要求，有时与 Header 平级，有时包装在 Body 中

        String requestJson = JSON.toJSONString(requestMap);
        log.info("建行生活推送订单请求报文: {}", requestJson);

        try {
            // 4. 发起 HTTP POST 请求
            // 提示：如果建行要求对整个 body 或特定字段进行 RSA/AES 加密，需在此处补充加密逻辑
            String responseStr = HttpUtil.post(apiUrl + "?txcode=A3341O031", requestJson, 5000);
            log.info("建行生活推送订单响应报文: {}", responseStr);

            // 5. 解析响应结果
            JSONObject responseJson = JSON.parseObject(responseStr);
            JSONObject respHeader = responseJson.getJSONObject("CLD_HEADER");

            if (respHeader != null && respHeader.containsKey("CLD_TX_RESP")) {
                JSONObject txResp = respHeader.getJSONObject("CLD_TX_RESP");
                String code = txResp.getString("CLD_CODE");
                String desc = txResp.getString("CLD_DESC");

                // 通常 "0" 或 "00000000" 代表成功
                if ("0".equals(code) || "00000000".equals(code)) {
                    return true;
                } else {
                    log.error("建行生活订单推送失败: [{}] {}", code, desc);
                    throw new CoBusinessException("建行订单同步失败: " + desc);
                }
            }
            return false;
        } catch (Exception e) {
            log.error("请求建行生活推送订单接口异常", e);
            throw new CoBusinessException("请求建行生活服务异常");
        }
    }

}
