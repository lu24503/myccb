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
import com.shop.cereshop.app.alioss.service.GetFileListService;
import com.shop.cereshop.app.config.ConstantProperties;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class GetFileListServiceImpl implements GetFileListService {

    private  String endpoint= ConstantProperties.ALIOSS_END_POINT;
    private  String accessKeyId= ConstantProperties.ALIOSS_ACCESS_KEY_ID;
    private  String accessKeySecret= ConstantProperties.ALIOSS_ACCESS_KEY_SECRET;
    private  String bucketName= ConstantProperties.ALIOSS_BUCKET_NAME;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<OSSObjectSummary> getDefaultFiles() {
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        ObjectListing objectListing = ossClient.listObjects(bucketName);
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        // 关闭OSSClient。
        ossClient.shutdown();
        return sums;
    }

    @Override
    public List<OSSObjectSummary> getFilesByKeys(int maxKeys) {
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        // 列举文件。
        ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withMaxKeys(maxKeys));
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        // 关闭OSSClient。
        ossClient.shutdown();
        return sums;
    }

    @Override
    public List<OSSObjectSummary> getFileByPrefix(String prefix) {
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        // 列举包含指定前缀的文件。默认列举100个文件。
        ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withPrefix(prefix));
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        // 关闭OSSClient。
        ossClient.shutdown();
        return sums;
    }

    @Override
    public List<OSSObjectSummary> getFilesNyMarker(String marker) {
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        // 列举指定marker之后的文件。默认列举100个文件。
        ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withMarker(marker));
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        // 关闭OSSClient。
        ossClient.shutdown();
        return sums;
    }

    @Override
    public List<OSSObjectSummary> getFilesPaging(int maxKeys) {
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        String nextMarker = null;
        ObjectListing objectListing;
        List<OSSObjectSummary> sums=null;
        do {
            objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withMarker(nextMarker).withMaxKeys(maxKeys));
            sums = objectListing.getObjectSummaries();
            nextMarker = objectListing.getNextMarker();

        } while (objectListing.isTruncated());
        // 关闭OSSClient。
        ossClient.shutdown();
        return sums;
    }

    @Override
    public List<OSSObjectSummary> getFilesPagingByPrefix(String prefix, int maxKeys) {
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        String nextMarker = null;
        ObjectListing objectListing;
        List<OSSObjectSummary> sums=null;
        do {
            objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).
                    withPrefix(prefix).withMarker(nextMarker).withMaxKeys(maxKeys));

            sums = objectListing.getObjectSummaries();
            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
        // 关闭OSSClient。
        ossClient.shutdown();
        return sums;
    }

    @Override
    public List<OSSObjectSummary> getFolderFiles() {
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        // 构造ListObjectsRequest请求。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        // 列出文件。
        ObjectListing listing = ossClient.listObjects(listObjectsRequest);
        // 遍历所有文件。
        System.out.println("Objects:");
//        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
//            System.out.println(objectSummary.getKey());
//        }
        // 遍历所有commonPrefix。
        System.out.println("CommonPrefixes:");
//        for (String commonPrefix : listing.getCommonPrefixes()) {
//            System.out.println(commonPrefix);
//        }
        // 关闭OSSClient。
        ossClient.shutdown();
        return listing.getObjectSummaries();
    }

    @Override
    public List<OSSObjectSummary> getFilesByFolder(String folder) {
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        // 构造ListObjectsRequest请求。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        // 设置prefix参数来获取fun目录下的所有文件。
//        listObjectsRequest.setPrefix("fun/");
        listObjectsRequest.setPrefix(folder);
        // 递归列出fun目录下的所有文件。
        ObjectListing listing = ossClient.listObjects(listObjectsRequest);
        // 遍历所有文件。
        System.out.println("Objects:");
//        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
//            System.out.println(objectSummary.getKey());
//        }
        // 遍历所有commonPrefix。
        System.out.println("\nCommonPrefixes:");
//        for (String commonPrefix : listing.getCommonPrefixes()) {
//            System.out.println(commonPrefix);
//        }
        // 关闭OSSClient。
        ossClient.shutdown();
        return listing.getObjectSummaries();
    }

    @Override
    public List<OSSObjectSummary> getFilesChildFolderByFolder(String folder) {
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        // 构造ListObjectsRequest请求。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        // 设置正斜线（/）为文件夹的分隔符。
        listObjectsRequest.setDelimiter("/");
        // 列出fun目录下的所有文件和文件夹。
//        listObjectsRequest.setPrefix("fun/");
        listObjectsRequest.setPrefix(folder);
        ObjectListing listing = ossClient.listObjects(listObjectsRequest);
        // 遍历所有文件。
        System.out.println("Objects:");
        // objectSummaries的列表中给出的是fun目录下的文件。
//        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
//            System.out.println(objectSummary.getKey());
//        }
        // 遍历所有commonPrefix。
        System.out.println("\nCommonPrefixes:");
        // commonPrefixs列表中给出的是fun目录下的所有子文件夹。fun/movie/001.avi和fun/movie/007.avi两个文件没有被列出来，因为它们属于fun文件夹下的movie目录。
//        for (String commonPrefix : listing.getCommonPrefixes()) {
//            System.out.println(commonPrefix);
//        }
        // 关闭OSSClient。
        ossClient.shutdown();
        return listing.getObjectSummaries();
    }
}
