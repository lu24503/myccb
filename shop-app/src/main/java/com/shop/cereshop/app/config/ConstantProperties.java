/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lightClouds917
 * Date 2018/1/16
 * Description:配置文件配置项
 */
@Configuration
public class ConstantProperties implements InitializingBean {

    /**
     * 阿里不同的服务器，地址不同
     */
    @Value("${alioss.file.endpoint}")
    private String alioss_file_endpoint;

    /**
     * OSS控制台获取
     */
    @Value("${alioss.file.keyid}")
    private String alioss_file_keyid;

    /**
     * OSS控制台获取
     */
    @Value("${alioss.file.keysecret}")
    private String alioss_file_keysecret;

    /**
     * 文件上传路径
     */
    @Value("${alioss.file.upload}")
    private String alioss_file_fileHost;

    /**
     * 这个自己创建bucket时的命名，控制台创建也行，代码创建也行
     */
    @Value("${alioss.file.bucketname}")
    private String alioss_file_bucketname;

    public static String ALIOSS_END_POINT;
    public static String ALIOSS_ACCESS_KEY_ID;
    public static String ALIOSS_ACCESS_KEY_SECRET;
    public static String ALIOSS_BUCKET_NAME;
    public static String ALIOSS_FILE_HOST;



    @Override
    public void afterPropertiesSet() throws Exception {
        ALIOSS_END_POINT = alioss_file_endpoint;
        ALIOSS_ACCESS_KEY_ID = alioss_file_keyid;
        ALIOSS_ACCESS_KEY_SECRET = alioss_file_keysecret;
        ALIOSS_BUCKET_NAME = alioss_file_bucketname;
        ALIOSS_FILE_HOST = alioss_file_fileHost;
    }
}
