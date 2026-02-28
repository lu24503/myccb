/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.message.service.miaoxin.impl;

import com.alibaba.fastjson.JSONObject;
import com.shop.cereshop.app.message.sdk.MiaoxinSmsClient;
import com.shop.cereshop.app.message.service.miaoxin.MiaoxinMessageService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.constant.StringEnum;
import com.shop.cereshop.commons.domain.message.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MiaoxinMessageServiceImpl implements MiaoxinMessageService {

    /**
     * 秒信云账号app_id
     */
    @Value("${miaoxinyun.account}")
    private String miaoxin_account;

    /**
     * 秒信云密钥
     */
    @Value("${miaoxinyun.secret}")
    private String miaoxin_secret;

    /**
     * 秒信云域名
     */
    @Value("${miaoxinyun.domain}")
    private String miaoxin_domain;

    /**
     * 秒信云签名
     */
    @Value("${miaoxinyun.sign_name}")
    private String signName;

    /**
     * 秒信云模板
     */
    @Value("${miaoxinyun.template}")
    private String template;

    @Override
    public String sendNotice(String phone, Map<String, String> map) throws Exception {
        MiaoxinSmsClient client = new MiaoxinSmsClient();
        String format = MessageFormat.format(template, map.values().toArray(new String[0]));
        String content = "【" + signName + "】" + format;
        client.setAccount(miaoxin_account);
        client.setPassword(miaoxin_secret);
        String result = client.send(phone, content, "ref", "103");
        log.info("result {}", result);
        System.out.println("result " + result);
        //返回结果转对象
        MessageResult messageResult = JSONObject.parseObject(result, MessageResult.class);
        return messageResult.getCode();
    }
}
