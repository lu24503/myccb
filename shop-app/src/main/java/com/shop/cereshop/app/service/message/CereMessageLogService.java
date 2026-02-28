/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.message;

import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.Map;

public interface CereMessageLogService {
    void save(String phone, String template, Map<String,String> map, String subject, String resultCode) throws CoBusinessException;
}
