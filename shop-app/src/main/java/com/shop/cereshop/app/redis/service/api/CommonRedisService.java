/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.redis.service.api;

public interface CommonRedisService {

    public boolean lock(String key);

    public void delete(String key);

    public boolean expire(String key, long time);

    public long getExpire(String key);

    public boolean hasKey(String key);

    public void del(String... key);
}
