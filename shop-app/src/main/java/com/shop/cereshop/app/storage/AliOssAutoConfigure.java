/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.storage;

import com.shop.cereshop.app.alioss.service.FileUploadService;
import com.shop.cereshop.commons.upload.strategy.FileStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 阿里OSS
 *
 * @author ceres
 * @date 2021/07/23
 */
@Configuration
@Slf4j
@ConditionalOnProperty(name = "upload.type", havingValue = "ALI")
public class AliOssAutoConfigure {

    @Service
    public class AliServiceImpl implements FileStrategy {

        @Autowired
        private FileUploadService fileUploadService;

        @Override
        public String upload(MultipartFile file) throws Exception {
            return fileUploadService.uploadFile(file.getOriginalFilename(),file.getInputStream(),file.getSize());
        }

        @Override
        public String uploadStream(String fileName, byte[] bytes, InputStream inputStream, long size) throws Exception {
            return fileUploadService.uploadFile(fileName,inputStream,size);
        }

        @Override
        public InputStream getInputStream(String url) {
            return null;
        }
    }
}
