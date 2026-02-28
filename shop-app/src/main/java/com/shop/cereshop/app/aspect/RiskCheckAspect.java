/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.aspect;


import com.shop.cereshop.app.aspect.risk.RiskCheckHandler;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.AppletPayUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 风控拦截 AOP
 */
@Aspect
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class RiskCheckAspect {

    private RiskCheckHandler riskCheckHandler;

    @Pointcut("@annotation(com.shop.cereshop.app.param.risk.RiskCheck)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Assert.notNull(request, "request can not null");
        Object[] args = pjp.getArgs();
        for (Object arg:args) {
            log.info("arg.class: " + arg.getClass());
        }

        String ip = AppletPayUtil.getClientIp(request);
        boolean check = riskCheckHandler.check(request, args);
        log.info("riskCheck ip [{}]", ip);

        if (!check) {
            log.info("riskCheck failed：ip [{}]", ip);
            return new Result(CoReturnFormat.ORDER_RISK);
        }

        // 执行进程
        return pjp.proceed();
    }

}
