/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.common.impl;

import com.shop.cereshop.app.service.common.CommonService;
import com.shop.cereshop.commons.utils.RandomStringUtil;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService {

    /**
     * 生成店铺编号
     * @return
     */
    @Override
    public String getShopCode() {
        return "SJ"+ RandomStringUtil.getRandomCode(8,0);
    }
}
