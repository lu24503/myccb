/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.redis.service.api;

public interface StringRedisService {

    public boolean set(String key, Object value);

    public boolean set(String key, Object value, long time);

    public long incr(String key, long delta);

    public long decr(String key, long delta);

    public Object get(String key);

    public void delete(String key);

    public boolean isExist(String key);

    public boolean lock(String key,String value);

    public boolean secKilllock(String key,String value);

    public void unlock(String key,String value);

    public void deleteAll();
}
