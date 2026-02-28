/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.OSSObjectSummary;
import com.shop.cereshop.app.alioss.service.DeleteFileService;
import com.shop.cereshop.app.alioss.service.GetFileListService;
import com.shop.cereshop.app.config.ConstantProperties;
import com.shop.cereshop.commons.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeleteFileServiceImpl implements DeleteFileService {

    @Autowired
    private GetFileListService getFileListService;

    private  String endpoint= ConstantProperties.ALIOSS_END_POINT;
    private  String accessKeyId= ConstantProperties.ALIOSS_ACCESS_KEY_ID;
    private  String accessKeySecret= ConstantProperties.ALIOSS_ACCESS_KEY_SECRET;
    private  String bucketName= ConstantProperties.ALIOSS_BUCKET_NAME;

    @Override
    public void deleteFiles(String url) {
        List<String> keys = new ArrayList<String>();
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        //获取文件名
        String prefix=url.substring(getIndex(url,"/",3)+1,url.length());
        ossClient.deleteObject(bucketName, prefix);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    public static int getIndex(String str, String c, int times) {
        int index = 0;
        String[] arr = str.split(c);
        int length = arr.length > times ? times : arr.length;
        for (int i = 0; i < length; i++) {
            index += arr[i].length();
        }
        return index + times - 1;
    }

    @Override
    public void deleteFiles(List<String> urls) {
        List<String> keys = new ArrayList<String>();
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        if(!EmptyUtils.isEmpty(urls)){
            for (String imageUrl:urls) {
                String prefix=imageUrl.substring(getIndex(imageUrl,"/",3)+1,imageUrl.length());
                List<OSSObjectSummary> list = getFileListService.getFileByPrefix(prefix);
                if(!EmptyUtils.isEmpty(list)){
                    keys.add(list.get(0).getKey());
                }
            }
            DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        }
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
