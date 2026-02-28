/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service;

import com.aliyun.oss.model.OSSObjectSummary;

import java.util.List;

public interface GetFileListService {

    /**
     * 列举存储空间下的文件（简单列举）默认100个文件
     */
    public List<OSSObjectSummary> getDefaultFiles();

    /**
     * 列举指定个数文件（ListObjectsRequest列举）
     * @param maxKeys 最大个数
     */
    public List<OSSObjectSummary> getFilesByKeys(int maxKeys);

    /**
     * 列举指定前缀文件（ListObjectsRequest列举）
     * @param prefix 前缀名称
     */
    public List<OSSObjectSummary> getFileByPrefix(String prefix);

    /**
     * 列举指定marker之后的文件（ListObjectsRequest列举）
     * @param marker 文件名称
     */
    public List<OSSObjectSummary> getFilesNyMarker(String marker);

    /**
     * 分页列举所有文件（ListObjectsRequest列举）
     * @param maxKeys 每页列举的个数
     */
    public List<OSSObjectSummary> getFilesPaging(int maxKeys);

    /**
     * 分页列举指定前缀的文件（ListObjectsRequest列举）
     * @param prefix 前缀名称
     * @param maxKeys 每页列举个数
     */
    public List<OSSObjectSummary> getFilesPagingByPrefix(String prefix, int maxKeys);

    /**
     * 列举指定存储空间下的所有文件(文件夹功能)
     */
    public List<OSSObjectSummary> getFolderFiles();

    /**
     * 列举指定目录下的所有文件(文件夹功能)
     * @param folder 指定目录名称（例如fun目录下所有文件传"fun/"）
     */
    public List<OSSObjectSummary> getFilesByFolder(String folder);

    /**
     * 列举指定目录下的文件和子目录(文件夹功能)
     * @param folder 指定目录名称（例如fun目录下所有文件传"fun/"）
     */
    public List<OSSObjectSummary> getFilesChildFolderByFolder(String folder);

}
