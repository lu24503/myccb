/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.discount;

import com.shop.cereshop.app.page.discount.DiscountIndex;
import com.shop.cereshop.app.param.discount.DiscountParam;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.discount.CereShopDiscountService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.EmptyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 限时折扣专区
 */
@RestController
@RequestMapping("discount")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "DiscountController")
@Api(value = "限时折扣专区", tags = "限时折扣专区")
public class DiscountController {

    @Autowired
    private CereShopDiscountService cereShopDiscountService;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    /**
     * 限时折扣专区首页数据查询
     * @return
     */
    @GetMapping("getIndex")
    @ApiOperation(value = "限时折扣专区首页数据查询")
    public Result<DiscountIndex> getIndex(DiscountParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        DiscountIndex index= cereShopDiscountService.getIndex(param,user);
        return new Result(index, CoReturnFormat.SUCCESS);
    }
}
