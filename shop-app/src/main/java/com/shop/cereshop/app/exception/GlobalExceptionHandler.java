/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.exception;

import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.EmptyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * 处理自定义的业务异常
	 * @param req
	 * @param e
	 * @return
	 */
    @ExceptionHandler(value = CoBusinessException.class)
    @ResponseBody
	public Result coBusinessExceptionHandler(HttpServletRequest req, CoBusinessException e){
		log.error("",e);
		if(!EmptyUtils.isEmpty(e.getRedissonLock())){
			//如果拿到分布式锁,解锁
			e.getRedissonLock().unlock();
		}
		if(!EmptyUtils.isEmpty(e.getErrorData())){
			return new Result(e.getCode(),e.getErrorData());
		}
    	return new Result(e.getCode());
    }

    /**
        * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
	@ResponseBody
	public Result exceptionHandler(HttpServletRequest req, Exception e){
		log.error("", e);
       	return new Result(CoReturnFormat.SYS_ERROR);
    }
}
