/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.redis;

import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CereRedisKeyServcice {
    void add(String key, String endTime) throws CoBusinessException;

    void updateByKey(String key, String endTime) throws CoBusinessException;

    void deleteByKey(String key) throws CoBusinessException;

    void initializationRedis() throws CoBusinessException,Exception;

    String findByKey(String key);
}
