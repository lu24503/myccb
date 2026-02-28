/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.aspect;

import com.shop.cereshop.app.service.log.CerePlatformWebLogService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.log.CerePlatformWebLog;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.AppletPayUtil;
import com.shop.cereshop.commons.utils.TimeUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 * 封装请求记录入库
 */
@Slf4j(topic = "SaveRequestAspect")
public class WebLogAspect {

    @Autowired
    private CerePlatformWebLogService cerePlatformWebLogService;

    /**
     * 定义接口请求开始时间
     */
    private long startTime;

    @Pointcut("@annotation(com.shop.cereshop.app.annotation.NoRepeatWebLog)")
    public void webLog() {
    }

    /**
     * 设置接口请求起始时间
     * @throws Throwable
     */
    @Before("webLog()")
    public void before() throws Throwable {
        this.startTime=System.currentTimeMillis();
    }

    /**
     * 插入请求记录数据
     * @param result
     * @return
     * @throws Throwable
     */
    @AfterReturning(returning = "result",pointcut = "webLog()")
    public Object after(Result result) throws Throwable {
        //封装请求记录
        CerePlatformWebLog webLog=new CerePlatformWebLog();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //获取浏览器信息
        String ua = request.getHeader("User-Agent");
        //转成UserAgent对象
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        //获取浏览器信息
        Browser browser = userAgent.getBrowser();
        //浏览器名称
        String browserName = browser.getName();
        webLog.setCreateTime(TimeUtils.yyMMddHHmmss());
        webLog.setType(IntegerEnum.REQUEST_TYPE_APP.getCode());
        webLog.setIp(AppletPayUtil.getClientIp(request));
        webLog.setUrl(request.getServletPath());
        webLog.setBrowserName(browserName);
        webLog.setParams(result.getJson());
        webLog.setName(result.getName());
        webLog.setDescribe(result.getDescribe());
        webLog.setIpSource(AppletPayUtil.getIpLocation(webLog.getIp()));
        webLog.setTime(Integer.parseInt(String.valueOf(System.currentTimeMillis()-startTime)));
        cerePlatformWebLogService.insert(webLog);
        return result;
    }
}
