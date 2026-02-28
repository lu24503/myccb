/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service;

import java.util.List;

public interface DeleteFileService {

    /**
     * 删除文件
     * @param url  文件路径
     */
    public void deleteFiles(String url);

    /**
     * 删除多个文件
     * @param urls  文件路径集合
     */
    public void deleteFiles(List<String> urls);
}
