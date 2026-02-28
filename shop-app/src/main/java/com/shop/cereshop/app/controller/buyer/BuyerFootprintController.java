/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.buyer;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.page.collect.BuyerFootprint;
import com.shop.cereshop.app.param.collect.FootprintIdParam;
import com.shop.cereshop.app.service.collect.CereBuyerFootprintService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.GsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 浏览足迹
 */
@RestController
@RequestMapping("footprint")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "BuyerFootprintController")
@Api(value = "浏览足迹", tags = "浏览足迹")
public class BuyerFootprintController {

    @Autowired
    private CereBuyerFootprintService cereBuyerFootprintService;

    /**
     * 删除足迹
     * @param param
     * @return
     */
    @RequestMapping(value = "delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "删除足迹")
    @NoRepeatWebLog
    public Result delete(@RequestBody FootprintIdParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerFootprintService.delete(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"删除足迹", GsonUtil.objectToGson(param));
    }

    /**
     * 选中足迹
     * @param param
     * @return
     */
    @RequestMapping(value = "selected", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "选中足迹")
    @NoRepeatWebLog
    public Result selected(@RequestBody FootprintIdParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerFootprintService.selected(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"选中足迹", GsonUtil.objectToGson(param));
    }

    /**
     * 我的足迹查询
     * @return
     */
    @GetMapping("getAll")
    @ApiOperation(value = "我的足迹查询")
    public Result<Page<BuyerFootprint>> getAll(PageParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page list=cereBuyerFootprintService.getAll(param,user.getBuyerUserId());
        return new Result(list, CoReturnFormat.SUCCESS);
    }
}
