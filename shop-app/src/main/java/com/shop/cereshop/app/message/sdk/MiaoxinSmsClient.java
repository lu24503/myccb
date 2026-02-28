/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.message.sdk;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 秒信短信业务客户端SDK
 *
 * @author miaoxin
 */
public class MiaoxinSmsClient extends MiaoxinClient {

    private String account = "abcabc";              //你的短信子账号App_ID（非登陆账号）
    private String secret = "123123123123123";      //你的短信子账号的App_SECRET 密匙（非登陆密码）
    private String server = "http://www.51miaoxin.com";

    /**
     * 发送全变量（自定义签名及内容）接口
     *
     * @param mobiles           接受短信用户的手机号码，多个手机用半角字符“,”分开
     * @param content           具体短信内容，以UTF-8方式传递
     * @param ref               客户可以对提交的短信加入reference参数以便后续进行跟踪
     * @param ext               客户的自定义扩展号码（与运营人员确认是否具有扩展码）
     * @return
     * @throws IOException
     */
    public String send(String mobiles, String content, String ref, String ext) throws IOException {
        String interfaceName = "/sms/send";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("mobiles", mobiles);
        params.put("content", content);
        params.put("ref", ref);
        params.put("ext", ext);
        String result = request(interfaceName, account, secret, params);
        //TODO 返回的JSON中，result数组即发送结果
        return result;
    }

    /**
     * 发送固定签名内容接口
     *
     * @param signatureId        签名ID
     * @param mobiles            接受短信用户的手机号码，多个手机用半角字符“,”分开
     * @param content            具体短信内容，以UTF-8方式传递
     * @param ref                客户可以对提交的短信加入reference参数以便后续进行跟踪
     * @param ext                客户的自定义扩展号码（与运营人员确认是否具有扩展码）
     * @return
     * @throws IOException
     */
    public String sendFixedSignature(String signatureId, String mobiles, String content, String ref, String ext) throws IOException {
        String interfaceName = "/sms/sendFixedSignature";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("signatureId", signatureId);
        params.put("mobiles", mobiles);
        params.put("content", content);
        params.put("ref", ref);
        params.put("ext", ext);
        String result = request(interfaceName, account, secret, params);
        //TODO 返回的JSON中，result数组即发送结果
        return result;
    }

    /**
     * 发送模板变量
     *
     * @param mobiles            接受短信用户的手机号码，多个手机用半角字符“,”分开
     * @param templateId         模板ID
     * @param ref                客户可以对提交的短信加入reference参数以便后续进行跟踪
     * @param ext                客户的自定义扩展号码（与运营人员确认是否具有扩展码）
     * @param paramValues        对应的参数
     * @return
     * @throws IOException
     */
    public String sendTemplateParamd(String mobiles, String templateId, String ref, String ext, String... paramValues) throws IOException {
        String interfaceName = "/sms/sendTemplateParamd";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("mobiles", mobiles);
        params.put("templateId", templateId);
        params.put("ref", ref);
        params.put("ext", ext);
        for (int i = 0; i < paramValues.length; i++) {
            params.put("param" + (i + 1), paramValues[i]);
        }
        String result = request(interfaceName, account, secret, params);
        //TODO 返回的JSON中，result数组即发送结果
        return result;
    }

    /**
     * 短信发送报告查询
     *
     * @param orderIds           可以一次查询一个订单或者多个订单，多个订单号用半角字符“,”分开
     * @return
     * @throws IOException
     */
    public String check(String orderIds) throws IOException {
        String interfaceName = "/sms/check";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("orderIds", orderIds);
        String result = request(interfaceName, account, secret, params);
        //TODO 返回的JSON中，reports即查询结果
        return result;
    }

    @Override
    public String getServerHost() {
        return server;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getPassword() {
        return secret;
    }

    /**
     * 设置链接的账号名
     * @param account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 设置链接的Server地址
     * @param server
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * 设置 链接的密码
     * @param secret
     */
    public void setPassword(String secret) {
        this.secret = secret;
    }
}
