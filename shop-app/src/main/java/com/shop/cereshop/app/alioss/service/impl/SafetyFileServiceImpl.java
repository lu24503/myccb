/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.BucketReferer;
import com.shop.cereshop.app.alioss.service.SafetyFileService;
import com.shop.cereshop.app.config.ConstantProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SafetyFileServiceImpl implements SafetyFileService {

    private  String endpoint= ConstantProperties.ALIOSS_END_POINT;
    private  String accessKeyId= ConstantProperties.ALIOSS_ACCESS_KEY_ID;
    private  String accessKeySecret= ConstantProperties.ALIOSS_ACCESS_KEY_SECRET;
    private  String bucketName= ConstantProperties.ALIOSS_BUCKET_NAME;

    @Override
    public void setSafety(List<String> refererList) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        // 设置存储空间Referer列表。设为true表示Referer字段允许为空。
        BucketReferer br = new BucketReferer(true, refererList);
        ossClient.setBucketReferer(bucketName, br);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public List<String> getSaftys() {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        // 获取存储空间Referer白名单列表。
        BucketReferer br = ossClient.getBucketReferer(bucketName);
        List<String> refererList = br.getRefererList();
        // 关闭OSSClient。
        ossClient.shutdown();
        return refererList;
    }

    @Override
    public void clearSafety() {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        // 防盗链不能直接清空，需要新建一个允许空Referer的规则来覆盖之前的规则。
        BucketReferer br = new BucketReferer();
        ossClient.setBucketReferer(bucketName, br);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
