/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.shop.cereshop.app.alioss.listener.GetObjectProgressListener;
import com.shop.cereshop.app.alioss.service.FileDownloadService;
import com.shop.cereshop.app.config.ConstantProperties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

@Service
public class FileDownloadServiceImpl implements FileDownloadService {

    private  String endpoint= ConstantProperties.ALIOSS_END_POINT;
    private  String accessKeyId= ConstantProperties.ALIOSS_ACCESS_KEY_ID;
    private  String accessKeySecret= ConstantProperties.ALIOSS_ACCESS_KEY_SECRET;
    private  String bucketName= ConstantProperties.ALIOSS_BUCKET_NAME;

    @Override
    public void downloadThis(String filename, String fileUrl) throws Exception{
        //获取文件名称接到目录地址后面
        fileUrl=fileUrl+"/"+filename.substring(filename.lastIndexOf("-")+1);
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        // 带进度条的下载。
        ossClient.getObject(new GetObjectRequest(bucketName, filename).
                        <GetObjectRequest>withProgressListener(new GetObjectProgressListener()),
                new File(fileUrl));
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public void downloadByStream(String filename) throws Exception{
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(bucketName, filename);
        // 读取文件内容。
        System.out.println("Object content:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:/picture/ceshi.jpg")), "utf-8"));
//        String str="";
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
//            str+=line;
            System.out.println("\n" + line);
        }
//        writer.write(str);
//        writer.flush();
        // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
        reader.close();
//        writer.close();
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public URL getUrl(String key) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        //设置过期时间为1小时
        Date expiration=new Date(new Date().getTime()+3600*10000);
        //生成URL
        URL url=ossClient.generatePresignedUrl(bucketName,key,expiration);
        return url;
    }
}
