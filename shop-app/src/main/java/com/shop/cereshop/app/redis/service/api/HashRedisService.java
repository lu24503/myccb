/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.redis.service.api;

import java.util.Map;

public interface HashRedisService {

    public Object hget(String key, String item);

    public Map<Object, Object> hmget(String key);

    public boolean hmset(String key, Map<String, Object> map);

    public boolean hmset(String key, Map<String, Object> map, long time);

    public boolean hset(String key, String item, Object value);

    public boolean hset(String key, String item, Object value, long time);

    public void hdel(String key, Object... item);

    public boolean hHasKey(String key, String item);

    public double hincr(String key, String item, double by);

    public double hdecr(String key, String item, double by);
}
