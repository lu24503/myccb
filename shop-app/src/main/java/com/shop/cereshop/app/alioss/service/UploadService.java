/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;

public interface UploadService {

    /**
     *上传本地文件(文件上传)
     * @param file 文件对象
     */
    public void uploadFile(File file) throws Exception;

    /**
     * 上传文件流（流式上传）
     * @param file 文件对象
     */
    public void uploadStream(File file) throws Exception;

    /**
     * 上传文件流（流式上传）
     * @param filename 文件名
     * @param inputStream 文件输入流
     */
    public void uploadStream(String filename, InputStream inputStream) throws Exception;

    /**
     * 上传网络流（流式上传）
     * @param file 文件对象
     */
    public void uploadInternetStream(File file) throws Exception;

    /**
     * 上传Byte数组（流式上传）
     * @param file 文件对象
     */
    public void uploadByte(File file) throws Exception;

    /**
     * 上传Byte数组（流式上传）
     * @param filename 文件名
     * @param content 文件字节数组
     */
    public String uploadByte(String filename, byte[] content) throws Exception;

    /**
     * 上传Byte数组（流式上传带进度条）
     * @param filename 文件名
     * @param content 文件字节数组
     * @param request  请求request
     * @param total 文件总大小
     */
    public void uploadByte(String filename, byte[] content, HttpServletRequest request, long total) throws Exception;

    /**
     * 上传Byte数组（流式上传带进度条），返回文件url
     * @param filename 文件名
     * @param content 文件字节数组
     * @param request  请求request
     * @param total 文件总大小
     */
    public String uploadByteReturnUrl(String filename, byte[] content, HttpServletRequest request, long total) throws Exception;

    /**
     * 上传Byte数组（流式上传）
     * @param file 文件对象
     */
    public void uploadString(File file) throws Exception;

    /**
     * 上传图片到OSS
     * @param filename 文件名
     * @param input2 文件流
     * @return
     */
    public void UploadFile(String filename, InputStream input2);

}
