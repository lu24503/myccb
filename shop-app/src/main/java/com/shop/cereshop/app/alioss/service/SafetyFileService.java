/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service;

import java.util.List;

public interface SafetyFileService {

    /**
     * 设置防盗链
     * @param refererList Referer白名单集合(允许指定的域名访问OSS资源，参数支持通配符星号（*）和问号（？）)
     */
    public void setSafety(List<String> refererList);

    /**
     * 获取防盗链信息
     */
    public List<String> getSaftys();

    /**
     * 清空防盗链
     */
    public void clearSafety();
}
