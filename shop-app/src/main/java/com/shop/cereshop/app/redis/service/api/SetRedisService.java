/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.redis.service.api;

import java.util.Set;

public interface SetRedisService {

    public Set<Object> sGet(String key);

    public boolean sHasKey(String key, Object value);

    public long sSet(String key, Object... values);

    public long sSetAndTime(String key, long time, Object... values);

    public long sGetSetSize(String key);

    public long setRemove(String key, Object... values);
}
