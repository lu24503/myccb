/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.shop.cereshop.app.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.shop.cereshop.commons.config.SecurityProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author /
 */
@Slf4j
@Configuration
public class TokenProvider implements InitializingBean {

    @Autowired
    private  SecurityProperties properties;

    @Autowired
    private  RedisTemplate redisTemplate;

    public static final String AUTHORITIES_KEY = "user";
    private JwtParser jwtParser;
    private JwtBuilder jwtBuilder;

//    public TokenProvider(SecurityProperties properties, StringRedisTemplate redisTemplate) {
//        this.properties = properties;
//        this.redisTemplate = redisTemplate;
//    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getBase64Secret());
        Key key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        jwtBuilder = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512);
    }

    /**
     * 创建Token 设置永不过期，
     *
     * @param username /
     * @return /
     */
    public String createToken(String username) {
        return properties.getTokenStartWith() + jwtBuilder
                // 加入ID确保生成的 Token 都不一致
                .setId(IdUtil.simpleUUID())
                .claim(AUTHORITIES_KEY, username)
                .setSubject(username)
                .compact();
    }

    /**
     * 创建refreshToken
     * @param username
     * @return
     */
    public String createRefreshToken(String username) {
        return properties.getTokenStartWith() + jwtBuilder
                // 加入ID确保生成的 Token 都不一致
                .setId(IdUtil.simpleUUID())
                .claim(AUTHORITIES_KEY, username)
                .setSubject(username)
                .compact();
    }



    /**
     * 依据Token 获取鉴权信息
     *
     * @param token /
     * @return /
     */
    String getTokenInfo(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public Claims getClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * @param token 需要检查的token
     */
    public void checkRenewal(String token) {
        // 判断是否续期token,计算token的过期时间
        long time = redisTemplate.getExpire(properties.getOnlineKey() + token) * 1000;
        Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        // 判断当前时间与过期时间的时间差
        long differ = expireDate.getTime() - System.currentTimeMillis();
        // 如果在续期检查的范围内，则续期
        if (differ <= properties.getDetect()) {
            long renew = time + properties.getRenew();
            redisTemplate.expire(properties.getOnlineKey() + token, renew, TimeUnit.MILLISECONDS);
        }
    }

    public String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(properties.getHeader());
        if (requestHeader != null && requestHeader.startsWith(properties.getTokenStartWith())) {
            return requestHeader.substring(7);
        }
        return null;
    }

    public String getToken(String token) {
        if (token != null && token.startsWith(properties.getTokenStartWith())) {
            return token.substring(7);
        }
        return null;
    }
}
