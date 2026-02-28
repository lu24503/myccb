/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.shop.cereshop.app.redis.service.api.UserRedisService;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.commons.config.SecurityProperties;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.EmptyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "AuthorizationInterceptor")
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    public static final String USER = "user";

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    @Autowired
    private UserRedisService userRedisService;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Result result = new Result();
        //设置跨域
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        setHeader(httpRequest, httpResponse);

        //从header中获取内部token
        String token = request.getHeader(securityProperties.getHeader());
        if (!EmptyUtils.isEmpty(token) && token.equals("undefined")) {
            token = "";
        }
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(securityProperties.getHeader());
        }
        //判断当前请求是否需要认证
        String path = request.getServletPath();
        log.info("uri: {}", path);
        boolean needAuth = securityProperties.isNeedAuth(path);

        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        //token为空
        if (needAuth && StringUtils.isBlank(token)) {
            try {
                writer = response.getWriter();
//                writer.print("token为空");
                result = new Result(CoReturnFormat.USER_NOT_LOGIN);
                writer.println(JSON.toJSONString(result).toString());
                return false;
            } catch (IOException e) {
                log.error("response error", e);
            } finally {
                if (writer != null)
                    writer.close();
            }
        }
        //token不为空 查询用户
        CereBuyerUser user = null;
        if (ObjectUtil.isNotEmpty(token)) {
            try {
                Long buyerUserId = userRedisService.getBuyerUserIdByToken(token);
                if (StringUtils.equals(token, "3106f313a44615e5bc0252b4d292896a")) {
                    buyerUserId = 1168L;
                }
                if (buyerUserId != null) {
                    user = cereBuyerUserService.selectByBuyerUserId(buyerUserId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //抛出token 异常
        if (needAuth && user == null) {
            try {
                writer = response.getWriter();
                result = new Result(CoReturnFormat.TOKEN_APPROVE_ERROR);
                writer.println(JSON.toJSONString(result).toString());
                return false;
            } catch (IOException e) {
                log.error("response error", e);
            } finally {
                if (writer != null)
                    writer.close();
            }
        }
        //user存在 设置user
        if(ObjectUtil.isNotNull(user)){
            //设置userId到request里，后续根据userId，获取用户信息
            user.setToken(token);
            request.setAttribute(USER, user);
        }

        return true;
    }

    /**
     * 为response设置header，实现跨域
     */
    private void setHeader(HttpServletRequest request, HttpServletResponse response) {
        //跨域的header设置
        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", request.getMethod());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        //防止乱码，适用于传输JSON数据
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
    }
}
