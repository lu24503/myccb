/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.shop.cereshop.app.alioss.listener.PutObjectProgressListener;
import com.shop.cereshop.app.alioss.service.FileDownloadService;
import com.shop.cereshop.app.alioss.service.GetFileListService;
import com.shop.cereshop.app.alioss.service.UploadService;
import com.shop.cereshop.app.config.ConstantProperties;
import com.shop.cereshop.commons.constant.StringEnum;
import com.shop.cereshop.commons.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private GetFileListService getFileListService;

    @Autowired
    private FileDownloadService fileDownloadService;

    /**
     * 阿里不同的服务器，地址不同
     */
    @Value("${alioss.file.endpoint}")
    private String endpoint;

    /**
     * OSS控制台获取
     */
    @Value("${alioss.file.keyid}")
    private String accessKeyId;

    /**
     * OSS控制台获取
     */
    @Value("${alioss.file.keysecret}")
    private String accessKeySecret;

    /**
     * 文件上传路径
     */
    @Value("${alioss.file.upload}")
    private String fileHost;

    /**
     * 这个自己创建bucket时的命名，控制台创建也行，代码创建也行
     */
    @Value("${alioss.file.bucketname}")
    private String bucketName;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void uploadFile(File file) throws Exception{
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        String dateStr = format.format(new Date());
        //创建文件路径
        String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+file.getName());
        ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(null,0)));
        //设置权限 这里是公开读
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public void uploadStream(File file) throws Exception {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        String dateStr = format.format(new Date());
        //创建文件路径
        String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+file.getName());
        // 上传文件流。
        InputStream inputStream = new FileInputStream(file);
        ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, inputStream).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(null,0)));
        //设置权限 这里是公开读
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public void uploadStream(String filename, InputStream inputStream) throws Exception {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        String dateStr = format.format(new Date());
        //创建文件路径
        String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+filename);
        // 上传文件流。
        ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, inputStream).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(null,0)));
        //设置权限 这里是公开读
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public void uploadInternetStream(File file) throws Exception{
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        String dateStr = format.format(new Date());
        //创建文件路径
        String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+file.getName());
        // 上传网络流。
        InputStream inputStream = new URL("https://www.aliyun.com/").openStream();
        ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, inputStream).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(null,0)));
        //设置权限 这里是公开读
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public void uploadByte(File file) throws Exception {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        String dateStr = format.format(new Date());
        //创建文件路径
        String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+file.getName());
        // 上传Byte数组。
        byte[] content = "Hello OSS".getBytes();
        ossClient.putObject(new PutObjectRequest(bucketName, fileUrl,new ByteArrayInputStream(content)).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(null,0)));
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public String uploadByte(String filename,byte[] content) throws Exception {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        String dateStr = format.format(new Date());
        //创建文件路径
        String fileUrl = fileHost + "/" + (dateStr + "/" + UUID.randomUUID().toString().replace("-","") + "_" + filename);
        ossClient.putObject(new PutObjectRequest(bucketName, fileUrl,new ByteArrayInputStream(content)).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(null,0)));
        ossClient.shutdown();

        return StringEnum.HTTPS_PREFIX.getCode() + ConstantProperties.ALIOSS_BUCKET_NAME + "." + ConstantProperties.ALIOSS_END_POINT + "/" + fileUrl;
    }

    public void uploadByte(String filename, byte[] content, HttpServletRequest request, long total) throws Exception {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        String dateStr = format.format(new Date());
        //创建文件路径
        String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+filename);
        long l = System.currentTimeMillis();
        ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, new ByteArrayInputStream(content)).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(request,total)));
        System.out.println(System.currentTimeMillis()-l);
        //设置权限 这里是公开读
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    public String uploadByteReturnUrl(String filename, byte[] content, HttpServletRequest request, long total) throws Exception {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        String dateStr = format.format(new Date());
        //创建文件路径
        String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+filename);
        long l = System.currentTimeMillis();
        ossClient.putObject(new PutObjectRequest(bucketName, fileUrl,new ByteArrayInputStream(content)).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(request,total)));
        System.out.println(System.currentTimeMillis()-l);
        //设置权限 这里是公开读
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        //上传成功后，获取文件访问路径
        String prefix=fileUrl.substring(0,fileUrl.indexOf("."));
        List<OSSObjectSummary> list = getFileListService.getFileByPrefix(prefix);
        if(!EmptyUtils.isEmpty(list)){
            URL url = fileDownloadService.getUrl(list.get(0).getKey());
            // 关闭OSSClient。
            ossClient.shutdown();
            return url.getProtocol()+"://"+url.getHost()+url.getPath();
        }
        return null;
    }

    @Override
    public void uploadString(File file) throws Exception {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        String dateStr = format.format(new Date());
        //创建文件路径
        String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+file.getName());
        // 上传字符串。
        String content = "Hello OSS";
        ossClient.putObject(new PutObjectRequest(bucketName, fileUrl,new ByteArrayInputStream(content.getBytes())).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(null,0)));
        //设置权限 这里是公开读
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public void UploadFile(String filename, InputStream input2) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        String dateStr = format.format(new Date());
        //创建文件路径
        String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+filename);
        // 上传网络流。
        PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, input2).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(null,0)));
        //设置权限 这里是公开读
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

}
