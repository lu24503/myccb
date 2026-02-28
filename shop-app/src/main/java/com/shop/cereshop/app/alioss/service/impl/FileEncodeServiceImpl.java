/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.shop.cereshop.app.alioss.service.FileEncodeService;
import com.shop.cereshop.app.config.ConstantProperties;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;

@Service
public class FileEncodeServiceImpl implements FileEncodeService {

    private  String endpoint= ConstantProperties.ALIOSS_END_POINT;
    private  String accessKeyId= ConstantProperties.ALIOSS_ACCESS_KEY_ID;
    private  String accessKeySecret= ConstantProperties.ALIOSS_ACCESS_KEY_SECRET;
    private  String bucketName= ConstantProperties.ALIOSS_BUCKET_NAME;
    ObjectListing objectListing;

    @Override
    public void encodeFile(int maxKeys, String keyPrefix, String nextMarker) throws Exception{
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
            listObjectsRequest.setPrefix(keyPrefix);
            listObjectsRequest.setMaxKeys(maxKeys);
            listObjectsRequest.setMarker(nextMarker);

            // 指定文件名称编码。
            listObjectsRequest.setEncodingType("url");

            objectListing = ossClient.listObjects(listObjectsRequest);

            // 文件解码。
            for (OSSObjectSummary objectSummary: objectListing.getObjectSummaries()) {
                System.out.println("Key:" + URLDecoder.decode(objectSummary.getKey(), "UTF-8"));
            }

            // commonPrefixes解码。
            for (String commonPrefixes: objectListing.getCommonPrefixes()) {
                System.out.println("CommonPrefixes:" + URLDecoder.decode(commonPrefixes, "UTF-8"));
            }

            // nextMarker解码。
            if (objectListing.getNextMarker() != null) {
                nextMarker = URLDecoder.decode(objectListing.getNextMarker(), "UTF-8");
            }
        } while (objectListing.isTruncated());

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
