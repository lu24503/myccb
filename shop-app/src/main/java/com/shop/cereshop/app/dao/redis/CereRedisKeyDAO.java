/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.redis;

import com.shop.cereshop.commons.domain.redis.CereRedisKey;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereRedisKeyDAO extends BaseMapper<CereRedisKey> {

    int insertSelective(CereRedisKey record);

    void updateByKey(CereRedisKey cereRedisKey);

    void deleteByKey(@Param("key") String key);

    List<CereRedisKey> findAll();

    String findByKey(@Param("key") String key);
}
