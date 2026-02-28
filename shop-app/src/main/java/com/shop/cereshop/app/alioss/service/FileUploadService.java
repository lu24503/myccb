/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service;

import java.io.InputStream;

public interface FileUploadService {

    String uploadFile(String var1,InputStream var7,long size) throws Exception;

    String uploadFile(String var1,byte[] var2) throws Exception;
}
