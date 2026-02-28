/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.message.service.aliyun.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.shop.cereshop.app.message.service.aliyun.AliyunMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AliyunMessageServiceImpl implements AliyunMessageService {

    /**
     * 阿里云区域id
     */
    @Value("${aliyun.regionid}")
    private String aliyun_region_id;

    /**
     * 阿里云产品名称
     */
    @Value("${aliyun.name}")
    private String aliyun_name;

    /**
     * 阿里云产品域名
     */
    @Value("${aliyun.domain}")
    private String aliyun_domain;

    /**
     * 阿里云产品keyid
     */
    @Value("${aliyun.keyid}")
    private String aliyun_keyid;

    /**
     * 阿里云产品密钥
     */
    @Value("${aliyun.keysecret}")
    private String aliyun_keysecret;

    /**
     * 阿里云签名
     */
    @Value("${aliyun.sign_name}")
    private String signName;

    /**
     * 阿里云模板编号
     */
    @Value("${aliyun.template}")
    private String template;

    @Override
    public String sendNotice(String phone, Map<String,String> map) throws Exception {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile(aliyun_region_id, aliyun_keyid, aliyun_keysecret);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);//注意这四行，与官网所展示的方法不同
        request.setDomain(aliyun_domain);
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", template);
        request.putQueryParameter("TemplateParam", JSON.toJSONString(map));
        CommonResponse response = acsClient.getCommonResponse(request);
        System.out.println(response.getData());

        return response.getData();
    }
}
