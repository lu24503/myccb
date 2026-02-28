/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.redis.service.api;

import java.util.List;

public interface ListRedisService {

    public List<Object> lGet(String key, long start, long end);

    public long lGetListSize(String key);

    public Object lGetIndex(String key, long index);

    public boolean lSet(String key, Object value);

    public boolean lSet(String key, Object value, long time);

    public boolean lSet(String key, List<Object> value);

    public boolean lSet(String key, List<Object> value, long time);

    public boolean lUpdateIndex(String key, long index, Object value);

    public long lRemove(String key, long count, Object value);
}
