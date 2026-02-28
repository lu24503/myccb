/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.aspect;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "RepeatSubmitAspect")
public class RepeatSubmitAspect {

    @Autowired
    private StringRedisService stringRedisService;

    @Pointcut("@annotation(com.shop.cereshop.app.annotation.NoRepeatSubmit)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Assert.notNull(request, "request can not null");
        // 此处可以用token
        String token = request.getHeader("Authorization");
        String key = token + "-" + request.getServletPath();
        NoRepeatSubmit annotation = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(NoRepeatSubmit.class);
        long expire = annotation.value();
        //超时时间：10秒，最好设为常量
        String time=String.valueOf(System.currentTimeMillis() + expire);
        //加锁
        boolean islock = stringRedisService.secKilllock(key, time);
        if (islock) {
            Object result;
            try {
                result = pjp.proceed();
            } finally {
                //解锁
                stringRedisService.unlock(key,time);
            }
            return result;
        }else {
            return new Result(CoReturnFormat.REPEAT_REQUEST);
        }
    }
}
