/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.buyer;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.page.collect.CollectProduct;
import com.shop.cereshop.app.page.collect.CollectShop;
import com.shop.cereshop.app.param.collect.CollectGetAllParam;
import com.shop.cereshop.app.param.collect.CollectIdParam;
import com.shop.cereshop.app.param.collect.CollectParam;
import com.shop.cereshop.app.service.collect.CereBuyerCollectService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
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
 * 我的收藏
 */
@RestController
@RequestMapping("collect")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "BuyerCollectController")
@Api(value = "我的收藏", tags = "我的收藏")
public class BuyerCollectController {

    @Autowired
    private CereBuyerCollectService cereBuyerCollectService;

    /**
     * 收藏商品、店铺
     * @param param
     * @return
     */
    @PostMapping("collect")
    @NoRepeatSubmit
    @ApiOperation(value = "收藏商品、店铺")
    @NoRepeatWebLog
    public Result collect(@RequestBody CollectParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerCollectService.collect(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"收藏商品、店铺", GsonUtil.objectToGson(param));
    }

    /**
     * 取消收藏
     * @param param
     * @return
     */
    @RequestMapping(value = "cancel", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "取消收藏")
    @NoRepeatWebLog
    public Result cancel(@RequestBody CollectIdParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerCollectService.cancel(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"取消收藏", GsonUtil.objectToGson(param));
    }

    /**
     * 取消收藏
     * @param param
     * @return
     */
    @RequestMapping(value = "new-cancel", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "取消收藏")
    @NoRepeatWebLog
    public Result newCancel(@RequestBody CollectParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerCollectService.newCancel(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"取消收藏", GsonUtil.objectToGson(param));
    }

    /**
     * 选中收藏商品、店铺
     * @param param
     * @return
     */
    @RequestMapping(value = "selected", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "选中收藏商品、店铺")
    @NoRepeatWebLog
    public Result selected(@RequestBody CollectIdParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerCollectService.selected(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"选中收藏商品、店铺", GsonUtil.objectToGson(param));
    }

    /**
     * 删除收藏商品、店铺
     * @param param
     * @return
     */
    @RequestMapping(value = "delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "删除")
    @NoRepeatWebLog
    public Result delete(@RequestBody CollectIdParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerCollectService.delete(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"删除收藏商品、店铺", GsonUtil.objectToGson(param));
    }

    /**
     * 收藏商品查询
     * @return
     */
    @GetMapping("getAllProduct")
    @ApiOperation(value = "收藏商品查询")
    public Result<Page<CollectProduct>> getAllProduct(CollectGetAllParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page page=cereBuyerCollectService.getAllProduct(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 收藏店铺查询
     * @return
     */
    @GetMapping("getAllShop")
    @ApiOperation(value = "收藏店铺查询")
    public Result<Page<CollectShop>> getAllShop(CollectGetAllParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page page=cereBuyerCollectService.getAllShop(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

}
