/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.redis.service.api;

import com.shop.cereshop.app.page.login.TokenInfoBo;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface UserRedisService {

    /** 保存用户id到redis中 */
    void saveUser(String token, String refreshToken, Long buyerUserId);

    /** 根据token获取用户id */
    Long getBuyerUserIdByToken(String token);

    /** 根据refreshToken获取用户id */
    Long getBuyerUserIdByRefreshToken(String refreshToken);

    /**
     * 刷新token
     * @param token
     */
    TokenInfoBo refreshToken(String token) throws CoBusinessException;

    /**
     * 生成token并且保存到缓存
     * @param username /
     * @param userId /
     * @return token
     */
    TokenInfoBo createToken(String username, Long userId);

}
