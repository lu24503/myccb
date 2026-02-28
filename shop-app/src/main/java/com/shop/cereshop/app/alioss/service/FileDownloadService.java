/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service;

import java.net.URL;

public interface FileDownloadService {

    /**
     * 下载到本地文件
     * @param filename 文件名地址
     * @param fileUrl  本地目录地址带文件名（存在相同文件名会覆盖，没有就创建）
     */
    public void downloadThis(String filename, String fileUrl) throws Exception;

    /**
     * 流式下载
     * @param filename 文件名地址
     */
    public void downloadByStream(String filename) throws Exception;

    /**
     * 获取oss文件下载路径
     * @param key
     * @return
     */
    public URL getUrl(String key);
}
