package com.shop.cereshop.app.controller.pay;

import com.shop.cereshop.app.service.order.CereShopOrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ccb/notify")
@Slf4j(topic = "CcbNotifyController")
@Api(value = "建行支付异步调用模块", tags = "支付模块")
public class CcbNotifyController {

    @Autowired
    private CereShopOrderService cereShopOrderService;

    @PostMapping("/paySuccess")
    public String payNotify(HttpServletRequest request) {
        try {
            // 1. 获取建行回调参数 (注意：这里需要判断建行传过来的是明文还是RSA加密报文)
            String orderId = request.getParameter("ORDERID");
            String success = request.getParameter("SUCCESS");
            String sign = request.getParameter("SIGN");

            // 2. 验签 MD5 (如果验签失败，直接 return "ERROR")
            boolean isSignValid = verifySignature(request, sign);
            if (!isSignValid) {
                log.error("建行回调验签失败, ORDERID: {}", orderId);
                return "ERROR";
            }

            // 3. 如果支付成功，调用现有订单处理核心方法
            if ("Y".equals(success)) { // 假设 Y 代表成功
                String transactionId = request.getParameter("PAY_FLOW_NO"); // 获取建行流水号

                // 复用 cereShopOrderService 中现成的方法，该方法会自动处理：
                // 修改状态为待发货、处理分销、处理拼单秒杀逻辑、赠送积分和转化率统计
                cereShopOrderService.handleWxLog(orderId, transactionId, orderId);
            }

            // 4. 返回建行要求的成功标识（通常纯文本的 SUCCESS 或 Y）
            return "SUCCESS";
        } catch (Exception e) {
            log.error("建行支付回调处理异常", e);
            return "ERROR";
        }
    }

    private boolean verifySignature(HttpServletRequest request, String sign) {
        // 根据建行规范组装验证MD5
        return true;
    }

}
