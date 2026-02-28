/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.storage;

import cn.hutool.core.util.StrUtil;
import com.shop.cereshop.commons.upload.strategy.FileStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;


/**
 * 本地上传配置
 *
 * @author ceres
 * @date 2021/07/23
 */
@Configuration
@ConditionalOnProperty(name = "upload.type", havingValue = "LOCAL")
@Slf4j
public class LocalAutoConfigure {

    public final static String DEFAULT_MONTH_FORMAT_SLASH = "yyyy/MM";

    @Service
    public class LocalServiceImpl implements FileStrategy {

        @Value("${upload.storage-path}")
        private String storagePath;

        @Value("${upload.uriPrefix}")
        private String uriPrefix;

        @Override
        public String upload(MultipartFile file) throws Exception {
            String originalFileName = file.getOriginalFilename();
            Objects.requireNonNull(originalFileName);
            originalFileName = originalFileName.replaceAll(" ", "");
            //生成文件名
            String fileName = UUID.randomUUID().toString().replace("-","") + "_" + originalFileName;

            //日期文件夹
            String relativePath = Paths.get("file", LocalDate.now().format(DateTimeFormatter.ofPattern(DEFAULT_MONTH_FORMAT_SLASH))).toString();
            // web服务器存放的绝对路径
            String absolutePath = Paths.get(storagePath, relativePath).toString();

            java.io.File outFile = new java.io.File(Paths.get(absolutePath, fileName).toString());
            FileUtils.writeByteArrayToFile(outFile, file.getBytes());

            String url = new StringBuilder(uriPrefix)
                    .append(relativePath)
                    .append("/")
                    .append(fileName)
                    .toString();
            //替换掉windows环境的\路径
            url = StrUtil.replace(url, "\\\\", "/");
            url = StrUtil.replace(url, "\\", "/");
            return url;
        }

        @Override
        public String uploadStream(String fileName, byte[] bytes, InputStream inputStream, long size) throws Exception {
            //生成文件名
            String newFileName = UUID.randomUUID().toString().replace("-","") + "_" + fileName;

            //日期文件夹
            String relativePath = Paths.get("file", LocalDate.now().format(DateTimeFormatter.ofPattern(DEFAULT_MONTH_FORMAT_SLASH))).toString();
            // web服务器存放的绝对路径
            String absolutePath = Paths.get(storagePath, relativePath).toString();

            java.io.File outFile = new java.io.File(Paths.get(absolutePath, newFileName).toString());
            FileUtils.writeByteArrayToFile(outFile, bytes);

            String url = new StringBuilder(uriPrefix)
                    .append(relativePath)
                    .append("/")
                    .append(newFileName)
                    .toString();
            //替换掉windows环境的\路径
            url = StrUtil.replace(url, "\\\\", "/");
            url = StrUtil.replace(url, "\\", "/");
            return url;
        }

        @Override
        public InputStream getInputStream(String url) {
            return null;
        }
    }
}
