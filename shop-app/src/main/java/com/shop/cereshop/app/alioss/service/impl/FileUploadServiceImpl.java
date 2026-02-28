/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;
import com.shop.cereshop.app.alioss.listener.PutObjectProgressListener;
import com.shop.cereshop.app.alioss.service.FileUploadService;
import com.shop.cereshop.app.config.ConstantProperties;
import com.shop.cereshop.commons.constant.StringEnum;
import com.shop.cereshop.commons.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String uploadFile(String filename, InputStream in,long size) throws Exception {
        log.info("filename {} size {}", filename, size);
        Objects.requireNonNull(filename);
        filename = filename.replaceAll(" ", "");
        String fileName= ConstantProperties.ALIOSS_FILE_HOST;
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(ConstantProperties.ALIOSS_END_POINT,
                ConstantProperties.ALIOSS_ACCESS_KEY_ID, ConstantProperties.ALIOSS_ACCESS_KEY_SECRET);
        String dateStr = format.format(new Date());
        //创建文件路径
        String fileUrl = fileName+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","") + "_" + filename);
        InputStream inputStream=in;
        if(size>5*1024*1024){
            //大于5M就压缩
            if(filename.contains("jpg")||filename.contains("png")){
                //如果是图片,压缩一下
                inputStream = ImageUtil.compressImg(in, 1.1f);
            }
        }
        // 上传网络流。
        ossClient.putObject(new PutObjectRequest(ConstantProperties.ALIOSS_BUCKET_NAME, fileUrl, inputStream).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(null,0)));

        ossClient.shutdown();
        if (inputStream != null) {
            inputStream.close();
        }
        if (in != null) {
            in.close();
        }
        return StringEnum.HTTPS_PREFIX.getCode() + ConstantProperties.ALIOSS_BUCKET_NAME + "." + ConstantProperties.ALIOSS_END_POINT + "/" + fileUrl;
    }

    @Override
    public String uploadFile(String filename,byte[] bytes) throws Exception {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(ConstantProperties.ALIOSS_END_POINT,
                ConstantProperties.ALIOSS_ACCESS_KEY_ID, ConstantProperties.ALIOSS_ACCESS_KEY_SECRET);
        String dateStr = format.format(new Date());
        //创建文件路径
        String fileUrl = ConstantProperties.ALIOSS_FILE_HOST+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","") + "_" + filename);
        long l = System.currentTimeMillis();
        ossClient.putObject(new PutObjectRequest(ConstantProperties.ALIOSS_BUCKET_NAME, fileUrl,new ByteArrayInputStream(bytes)).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(null,0)));

        return StringEnum.HTTPS_PREFIX.getCode() + ConstantProperties.ALIOSS_BUCKET_NAME + "." + ConstantProperties.ALIOSS_END_POINT + "/" + fileUrl;
    }
}
