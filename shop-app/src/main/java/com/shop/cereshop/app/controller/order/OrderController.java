/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.order.*;
import com.shop.cereshop.app.page.settlement.Settlement;
import com.shop.cereshop.app.param.comment.CommentSaveParam;
import com.shop.cereshop.app.param.order.*;
import com.shop.cereshop.app.param.risk.RiskCheck;
import com.shop.cereshop.app.param.settlement.SettlementParam;
import com.shop.cereshop.app.pay.weixin.skd.HttpKit;
import com.shop.cereshop.app.pay.weixin.skd.PaymentKit;
import com.shop.cereshop.app.pay.weixin.skd.WXPayUtil;
import com.shop.cereshop.app.pay.weixin.skd.WXPayV3Util;
import com.shop.cereshop.app.service.dict.CerePlatformDictService;
import com.shop.cereshop.app.service.order.CereShopOrderService;
import com.shop.cereshop.commons.config.AlipayConfig;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.StringEnum;
import com.shop.cereshop.commons.constant.WxPayEnum;
import com.shop.cereshop.commons.domain.after.CereAfterDilever;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.dict.CerePlatformDict;
import com.shop.cereshop.commons.domain.order.CereShopOrder;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.AppletPayUtil;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.GsonUtil;
import com.shop.cereshop.commons.utils.PayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单模块
 */
@RestController
@RequestMapping("order")
@Slf4j(topic = "OrderController")
@Api(value = "订单模块", tags = "订单模块")
public class OrderController {

    @Autowired
    private CereShopOrderService cereShopOrderService;

    @Autowired
    private CerePlatformDictService cerePlatformDictService;

    @Autowired
    private WXPayV3Util wxPayV3Util;

    /**
     * 商户秘钥
     */
    @Value("${weixin.key}")
    private String key;

    /**
     * 结算查询
     * @param param /
     * @return Settlement
     */
    @PostMapping("getSettlement")
    @ApiOperation(value = "结算查询")
    public Result<Settlement> getSettlement(@RequestBody SettlementParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Settlement settlement=cereShopOrderService.getSettlement(param,user);
        return new Result<>(settlement, CoReturnFormat.SUCCESS);
    }

    /**
     * 提交订单
     * @param param /
     * @return /
     */
    @PostMapping("submit")
    @NoRepeatSubmit
    @ApiOperation(value = "提交订单")
    @NoRepeatWebLog
    @RiskCheck
    public Result<PayUrl> submit(@RequestBody OrderParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        String ip= AppletPayUtil.getClientIp(request);
        PayUrl payUrl=cereShopOrderService.submit(param,user,ip);
        return new Result<>(payUrl,CoReturnFormat.SUCCESS,user.getWechatName(),"提交订单", GsonUtil.objectToGson(param));
    }

    /**
     * 根据订单id获取收款码
     * @param param /
     * @return /
     */
    @GetMapping("getUrl")
    @NoRepeatSubmit
    @ApiOperation(value = "根据订单id获取收款码")
    public Result<PayUrl> getUrl(OrderGetByIdParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        String ip= AppletPayUtil.getClientIp(request);
        PayUrl payUrl=cereShopOrderService.getUrl(param,user,ip);
        return new Result<>(payUrl,CoReturnFormat.SUCCESS);
    }

    /**
     * 校验当前订单是否支付成功
     * @param param
     * @return
     */
    @GetMapping("checkPay")
    @NoRepeatSubmit
    @ApiOperation(value = "校验当前订单是否支付成功")
    public Result<PayUrl> checkPay(PayParam param) throws CoBusinessException{
        PayUrl payUrl=cereShopOrderService.checkPay(param);
        return new Result<>(payUrl,CoReturnFormat.SUCCESS);
    }

    /**
     * 订单管理查询
     * @param param
     * @return
     */
    @GetMapping("getAll")
    @ApiOperation(value = "订单管理查询")
    public Result<Page<Orders>> getAll(OrderGetAllParam param,HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page page=cereShopOrderService.getAll(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 订单详情查询
     * @param param
     * @return
     */
    @GetMapping("getById")
    @ApiOperation(value = "订单详情查询")
    public Result<OrderDetail> getById(OrderGetByIdParam param) throws CoBusinessException,Exception{
        OrderDetail detail=cereShopOrderService.getById(param);
        return new Result(detail,CoReturnFormat.SUCCESS);
    }

    /**
     * 小程序立即支付
     * @param param
     * @return
     */
    @PostMapping("gotoPay")
    @NoRepeatSubmit
    @ApiOperation(value = "小程序立即支付")
    @NoRepeatWebLog
    public Result<OrderPay> gotoPay(@RequestBody PayParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        String ip= AppletPayUtil.getClientIp(request);
        OrderPay pay = cereShopOrderService.gotoPay(param,user,ip);
        return new Result(pay,CoReturnFormat.SUCCESS,user.getWechatName(),"小程序立即支付", GsonUtil.objectToGson(param));
    }

    @PostMapping("paySuccess")
    @NoRepeatSubmit
    @ApiOperation(value = "小程序支付成功")
    @NoRepeatWebLog
    public Result paySuccess(@RequestBody PayParam param,HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        String ip= AppletPayUtil.getClientIp(request);
        cereShopOrderService.paySuccess(param,user,ip);
        return new Result(CoReturnFormat.SUCCESS);
    }

    /**
     * 填写退货物流单
     * @param dilever
     * @return
     */
    @PostMapping("returnExpress")
    @NoRepeatSubmit
    @ApiOperation(value = "填写退货物流单")
    @NoRepeatWebLog
    public Result returnExpress(@RequestBody CereAfterDilever dilever, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopOrderService.returnExpress(dilever,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"填写退货物流单", GsonUtil.objectToGson(dilever));
    }

    /**
     * 选择物流公司查询
     * @return
     */
    @GetMapping("getExpressSelect")
    @ApiOperation(value = "选择物流公司查询")
    public Result<List<CerePlatformDict>> getExpressSelect() throws CoBusinessException{
        List<CerePlatformDict> list =cerePlatformDictService.getExpressSelect();
        return new Result(list,CoReturnFormat.SUCCESS);
    }

    /**
     * APP立即支付
     * @param param
     * @return
     */
    @PostMapping("gotoAppPay")
    @NoRepeatSubmit
    @ApiOperation(value = "APP立即支付")
    @NoRepeatWebLog
    public Result<OrderPay> gotoAppPay(@RequestBody PayParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        String ip= AppletPayUtil.getClientIp(request);
        OrderPay pay = cereShopOrderService.gotoAppPay(param,user,ip);

        return new Result(pay,CoReturnFormat.SUCCESS,user.getWechatName(),"APP立即支付", GsonUtil.objectToGson(param));
    }

    /**
     * H5立即支付
     * @param param
     * @return
     */
    @PostMapping("gotoH5Pay")
    @NoRepeatSubmit
    @ApiOperation(value = "H5立即支付")
    @NoRepeatWebLog
    public Result<OrderPay> gotoH5Pay(@RequestBody PayParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        String ip = AppletPayUtil.getClientIp(request);
        OrderPay pay = cereShopOrderService.gotoH5Pay(param,user,ip);
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.addHeader("Access-Control-Allow-Origin","*");
        return new Result(pay,CoReturnFormat.SUCCESS,user.getWechatName(),"H5立即支付", GsonUtil.objectToGson(param));
    }

    /**
     * 确认收货
     * @param param
     * @return
     */
    @RequestMapping(value = "confirm", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "确认收货")
    @NoRepeatWebLog
    public Result confirm(@RequestBody OrderGetByIdParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopOrderService.confirm(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"确认收货", GsonUtil.objectToGson(param));
    }

    /**
     * 取消订单
     * @param param
     * @return
     */
    @RequestMapping(value = "cancel", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "取消订单")
    @NoRepeatWebLog
    public Result cancel(@RequestBody OrderGetByIdParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopOrderService.cancel(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"取消订单", GsonUtil.objectToGson(param));
    }

    /**
     * 删除订单
     * @param param
     * @return
     */
    @RequestMapping(value = "delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "删除订单")
    @NoRepeatWebLog
    public Result delete(@RequestBody OrderGetByIdParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopOrderService.delete(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"删除订单", GsonUtil.objectToGson(param));
    }

    /**
     * 添加评论
     * @param param
     * @return
     */
    @PostMapping("addComment")
    @NoRepeatSubmit
    @ApiOperation(value = "添加评论")
    @NoRepeatWebLog
    public Result addComment(@RequestBody CommentSaveParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopOrderService.addComment(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"添加评论", GsonUtil.objectToGson(param));
    }

    /**
     * 追加评论
     * @param param
     * @return
     */
    @RequestMapping(value = "addToComment", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "追加评论")
    @NoRepeatWebLog
    public Result addToComment(@RequestBody CommentSaveParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopOrderService.addToComment(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"追加评论", GsonUtil.objectToGson(param));
    }

    /**
     * 申请退款
     * @param param
     * @return
     */
    @GetMapping("refund")
    @NoRepeatSubmit
    @ApiOperation(value = "申请退款")
    @NoRepeatWebLog
    public Result<List<CartSku>> refund(OrderGetByIdParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        List<CartSku> list=cereShopOrderService.refund(param,user);
        return new Result(list,CoReturnFormat.SUCCESS,user.getWechatName(),"申请退款", GsonUtil.objectToGson(param));
    }

    /**
     * 微信支付回调
     * @param request
     * @param response
     * @throws Exception
     */
    @PostMapping("pay/rolBack")
    public void wxProPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("进入微信小程序支付回调");
        String xmlMsg = HttpKit.readData(request);
        log.info("微信小程序通知信息"+xmlMsg);
        Map<String, String> resultMap = PaymentKit.xmlToMap(xmlMsg);
        if(!EmptyUtils.isEmpty(resultMap)){
            if(resultMap.get(StringEnum.WX_RETURN_CODE.getCode()).equals(StringEnum.WX_PAY_SUCCESS.getCode())){
                String orderNo = resultMap.get("out_trade_no");
                log.info("回调out_trade_no={} resultMap={}", orderNo, JSON.toJSONString(resultMap));
                //验证签名
                if(WXPayUtil.isSignatureValid(resultMap,key)){
                    log.info("回调out_trade_no="+orderNo);
                    log.info("微信小程序支付回调成功");
                    //截取订单编号
                    String[] split = orderNo.split("-");
                    if(!EmptyUtils.isEmpty(split)){
                        String orderFormId=split[0];
                        if(!EmptyUtils.isEmpty(orderFormId)){
                            //支付有礼
                            cereShopOrderService.payGift(orderFormId);

                            //处理支付成功后的其他逻辑
                            cereShopOrderService.handleWxLog(orderFormId,resultMap.get("transaction_id"),orderNo);
                        }

                    }
                } else {
                    log.info("signature not valid");
                }
            }
        } else {
            log.info("map为null");
        }
        String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        response.getWriter().write(result);
    }

    /**
     * 微信V3支付或退款回调
     * @param request
     * @param response
     * @throws Exception
     */
    @PostMapping("pay/v3RolBack")
    public void wxProV3Notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("v3RolBack 进入微信appV3回调");
        String body = HttpKit.readPureBodyData(request);
        log.info("v3RolBack appV3通知信息 {}", body);
        JSONObject retObj = JSON.parseObject(body);
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String sign = request.getHeader("Wechatpay-Signature");
        if(!EmptyUtils.isEmpty(retObj) && !EmptyUtils.isEmpty(timestamp)
            && !EmptyUtils.isEmpty(nonce) && !EmptyUtils.isEmpty(sign)){
            String eventType = retObj.getString("event_type");
            String summary = retObj.getString("summary");
            String decryptedText = wxPayV3Util.decrypt(timestamp, nonce, sign, body);
            log.info("v3RolBack decryptedText {}", decryptedText);
            switch (eventType) {
                case "TRANSACTION.SUCCESS":
                    try {
                        if (StringUtils.isNotBlank(decryptedText)) {
                            JSONObject resultObj = JSON.parseObject(decryptedText);
                            String tradeState = resultObj.getString("trade_state");
                            if (WxPayEnum.PAY_SUCCESS.getCode().equals(tradeState)) {
                                String transactionId = resultObj.getString("transaction_id");
                                String outTradeNo = resultObj.getString("out_trade_no");
                                String[] split = outTradeNo.split("-");
                                if(!EmptyUtils.isEmpty(split)){
                                    String orderFormId=split[0];
                                    if(!EmptyUtils.isEmpty(orderFormId)){
                                        //支付有礼
                                        cereShopOrderService.payGift(orderFormId);
                                        //处理支付成功后的其他逻辑
                                        cereShopOrderService.handleWxLog(orderFormId, transactionId, outTradeNo);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("v3RolBack TRANSACTION.SUCCESS decrypt fail " + e.getMessage(), e);
                        response.setStatus(500);
                        Map<String, String> map = new HashMap<>();
                        map.put("code", "FAIL");
                        map.put("message", "解密失败");
                        response.getWriter().write(JSON.toJSONString(map));
                    }
                    break;
                // 退款回调
                case "REFUND.SUCCESS":
                    try {
                        if (StringUtils.isNotBlank(decryptedText)) {
                            JSONObject resultObj = JSON.parseObject(decryptedText);
                            String refundStatus = resultObj.getString("refund_status");
                            String outTradeNo = resultObj.getString("out_trade_no");
                            String transactionId = resultObj.getString("transaction_id");
                            if (WxPayEnum.REFUND_OK.getCode().equals(refundStatus)) {
                                String[] split = outTradeNo.split("-");
                                if (!EmptyUtils.isEmpty(split)) {
                                    String orderFormId = split[0];
                                    if (!EmptyUtils.isEmpty(orderFormId)) {
                                        cereShopOrderService.handleRefundWxLog(orderFormId, transactionId, outTradeNo);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("v3RolBack REFUND.SUCCESS decrypt fail " + e.getMessage(), e);
                        response.setStatus(500);
                        Map<String, String> map = new HashMap<>();
                        map.put("code", "FAIL");
                        map.put("message", "解密失败");
                        response.getWriter().write(JSON.toJSONString(map));
                    }
                    break;
            }
        } else {
            log.info("map为null");
        }
    }

    /**
     * 微信退款回调
     * @param request
     * @param response
     * @throws Exception
     */
    @PostMapping("refund/rolBack")
    public void wxRefundNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("进入微信小程序退款回调");
        String xmlMsg = HttpKit.readData(request);
        log.info("微信小程序通知信息"+xmlMsg);
        Map<String, String> resultMap = PaymentKit.xmlToMap(xmlMsg);
        if(!EmptyUtils.isEmpty(resultMap)){
            if(resultMap.get(StringEnum.WX_RETURN_CODE.getCode()).equals(StringEnum.WX_PAY_SUCCESS.getCode())){
                //获取微信返回数据进行解密
                String req_info = resultMap.get("req_info");
                String str= EmptyUtils.decryption(req_info,key);
                Map<String, String> map = PaymentKit.xmlToMap(str);
                String orderNo = map.get("out_trade_no");
                log.info("回调out_trade_no={} resultMap={}", orderNo, JSON.toJSONString(resultMap));
                //if(WXPayUtil.isSignatureValid(resultMap,key)){
                    //验证签名
                    log.info("回调out_trade_no="+orderNo);
                    log.info("微信小程序退款回调成功");
                    //截取订单编号
                    String[] split = orderNo.split("-");
                    if(!EmptyUtils.isEmpty(split)){
                        String orderFormId=split[0];
                        if(!EmptyUtils.isEmpty(orderFormId)){
                            cereShopOrderService.handleRefundWxLog(orderFormId,map.get("transaction_id"),orderNo);
                        }
                    }
                //}
            }
        }else {
            log.info("map为null");
        }
        String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        response.getWriter().write(result);
    }

    /**
     * 支付宝小程序支付回调
     * @param request
     * @throws Exception
     */
    @PostMapping("alipay/rolBack")
    @ResponseBody
    public String alipayNotify(HttpServletRequest request) {
        log.info("进入支付宝小程序支付回调");
        Map< String , String > params = PayUtil.getAlipayResultParams(request);
        log.info("支付宝小程序回调信息:"+params);

        try {
            if(!EmptyUtils.isEmpty(params) && PayUtil.signAlipayVerified(params)){
                String tradeStatus = params.get(StringEnum.ALI_PAY_TRADE_STATUS.getCode());
                if(StringEnum.ALI_PAY_SUCCESS.getCode().equals(tradeStatus) || StringEnum.ALI_PAY_FINISHED.getCode().equals(tradeStatus)){
                    String refundFee = params.get("refund_fee");
                    //由于支付宝部分退款也走了 支付的回调接口，所以做此处理
                    if (StringUtils.isNotBlank(refundFee)) {
                        //售后审核通过的退款，暂时由商家端处理 支付宝退款回调
                        //客户端取消订单发起退款，不是部分退款，所以不会回调到这里
                    } else {
                        String orderNo = params.get("out_trade_no");
                        //支付有礼
                        String[] split = orderNo.split("-");
                        if(!EmptyUtils.isEmpty(split)){
                            String orderFormId = split[0];
                            cereShopOrderService.payGift(orderFormId);
                        }

                        //处理支付成功后的其他逻辑
                        payNotifyInner(orderNo,params.get("trade_no"));
                    }
                    return "success";
                } else {
                    log.error("交易状态异常");
                }
            } else {
                log.error("map为null 或 签名不通过");
            }
        } catch (Exception e) {
            log.error("alipayNotify failed: params = {}", params, e);
        }
        return "failure";
    }

    /**
     * 支付回调业务逻辑
     * @param orderNo
     * @param transactionId
     */
    private void payNotifyInner(String orderNo, String transactionId) throws Exception {
        log.info("支付宝支付回调out_trade_no="+orderNo);
        //截取订单编号
        String[] split = orderNo.split("-");
        if(!EmptyUtils.isEmpty(split)){
            String orderFormId=split[0];
            if(!EmptyUtils.isEmpty(orderFormId)){
                cereShopOrderService.handleWxLog(orderFormId,transactionId,orderNo);
            }
        }
    }

    /**
     * 暂时废弃-支付宝部分退款回调走了 支付的回调地址
     * 支付宝退款回调
     * @param request
     * @throws Exception
     */
    @PostMapping("alipayRefund/rolBack")
    @Deprecated
    public String alipayRefundNotify(HttpServletRequest request) throws Exception {
        log.info("进入支付宝退款回调");
        Map<String, String> params = PayUtil.getAlipayResultParams(request);
        log.info("支付宝退款回调信息:"+params);

        if(!EmptyUtils.isEmpty(params) && PayUtil.signAlipayVerified(params)){
            String orderNo = params.get("out_trade_no");
            log.info("支付宝退款回调out_trade_no="+orderNo);
            //截取订单编号
            /*String[] split = orderNo.split("-");
            if(!EmptyUtils.isEmpty(split)){
                String orderFormId=split[0];
                if(!EmptyUtils.isEmpty(orderFormId)){
                    cereShopOrderService.handleRefundWxLog(orderFormId,params.get("trade_no"),orderNo);
                }
            }*/
        } else {
            log.info("map为null 或 签名不通过");
            return "failure";
        }
        return "success";
    }

    /**
     * 物流信息查询
     * @param param
     * @return
     */
    @GetMapping("getDilevery")
    @ApiOperation(value = "物流信息查询")
    public Result<List<Dilevery>> getDilevery(DileveryParam param) throws CoBusinessException,Exception{
        List<Dilevery> dileveries=cereShopOrderService.getDilevery(param);
        return new Result(dileveries,CoReturnFormat.SUCCESS);
    }

    /**
     * 支付业务处理接口
     * @param param
     * @return
     */
    /*@PostMapping(value = "pay")
    @ApiOperation(value = "支付业务处理接口")
    public Result pay(@RequestBody PayParam param) throws CoBusinessException,Exception{
        cereShopOrderService.pay(param);
        return new Result(CoReturnFormat.SUCCESS);
    }*/

    /**
     * 查询花呗手续费配置
     * @return
     */
    @GetMapping(value = "getHuabeiConfig")
    @ApiOperation(value = "查询花呗手续费配置")
    public Result getHuabeiFeeRateList() {
        Map<String,Object> map = new HashMap<>();
        map.put("huabeiFeeRateList", AlipayConfig.HUABEI_FEERATE_LIST);
        map.put("huabeiChargeType", AlipayConfig.HUABEI_CHARGE_TYPE);
        return new Result(map, CoReturnFormat.SUCCESS);
    }

    /**
     * 查询支付有礼数据
     * @return
     */
    @GetMapping(value = "getOrderPolite")
    @ApiOperation(value = "查询支付有礼数据")
    public Result<OrderPoliteDTO> getOrderPolite(HttpServletRequest request, CereShopOrder orderParam) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Long buyerUserId = null;
        if (user != null) {
            buyerUserId = user.getBuyerUserId();
        }
        OrderPoliteDTO politeDTO = cereShopOrderService.getOrderPolite(buyerUserId, orderParam.getOrderId());
        return new Result(politeDTO, CoReturnFormat.SUCCESS);
    }
}
