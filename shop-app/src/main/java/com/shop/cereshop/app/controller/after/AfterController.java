/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.after;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.page.after.AfterDetail;
import com.shop.cereshop.app.page.after.Afters;
import com.shop.cereshop.app.param.after.AfterGetAllParam;
import com.shop.cereshop.app.param.after.AfterGetByIdParam;
import com.shop.cereshop.app.param.after.AfterParam;
import com.shop.cereshop.app.param.after.PlatformParam;
import com.shop.cereshop.app.param.common.CommonIdParam;
import com.shop.cereshop.app.service.after.CereOrderAfterService;
import com.shop.cereshop.app.service.dict.CerePlatformDictService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.dict.CerePlatformDict;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.GsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * 售后模块
 */
@RestController
@RequestMapping("after")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "AfterController")
@Api(value = "售后模块", tags = "售后模块")
public class AfterController {

    @Autowired
    private CereOrderAfterService cereOrderAfterService;

    @Autowired
    private CerePlatformDictService cerePlatformDictService;

    /**
     * 获取要退款的订单金额
     * @param param
     * @return
     */
    @PostMapping("getReturnPrice")
    @ApiOperation(value = "获取要退款的订单金额")
    public Result getReturnPrice(@RequestBody AfterParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        BigDecimal returnPrice = cereOrderAfterService.getReturnPrice(param, user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"获取要退款的订单金额", GsonUtil.objectToGson(returnPrice));
    }

    /**
     * 申请售后
     * @param param
     * @return
     */
    @PostMapping("submit")
    @NoRepeatSubmit
    @ApiOperation(value = "申请售后")
    @NoRepeatWebLog
    public Result submit(@RequestBody AfterParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereOrderAfterService.submit(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"申请售后", GsonUtil.objectToGson(param));
    }

    /**
     * 申请平台介入
     * @param param
     * @return
     */
    @PostMapping("platform")
    @NoRepeatSubmit
    @ApiOperation(value = "申请平台介入")
    @NoRepeatWebLog
    public Result platform(@RequestBody PlatformParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereOrderAfterService.platform(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"申请平台介入", GsonUtil.objectToGson(param));
    }

    /**
     * 售后列表查询
     * @param param
     * @return
     */
    @GetMapping("getAll")
    @ApiOperation(value = "售后列表查询")
    public Result<Page<Afters>> getAll(AfterGetAllParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page page=cereOrderAfterService.getAll(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 售后详情查询
     * @param param
     * @return
     */
    @GetMapping("getById")
    @ApiOperation(value = "售后列表查询")
    public Result<AfterDetail> getById(AfterGetByIdParam param) throws CoBusinessException,Exception{
        AfterDetail detail=cereOrderAfterService.getById(param.getAfterId());
        return new Result(detail,CoReturnFormat.SUCCESS);
    }

    /**
     * 撤销申请
     * @param param
     * @return
     */
    @RequestMapping(value = "returnRefund", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "撤销申请")
    @NoRepeatWebLog
    public Result returnRefund(@RequestBody AfterGetByIdParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereOrderAfterService.returnRefund(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"撤销申请", GsonUtil.objectToGson(param));
    }

    /**
     * 撤销退货
     * @param param
     * @return
     */
    @RequestMapping(value = "returnGoods", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "撤销退货")
    @NoRepeatWebLog
    public Result returnGoods(@RequestBody AfterGetByIdParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereOrderAfterService.returnGoods(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"撤销退货", GsonUtil.objectToGson(param));
    }

    /**
     * 选择退款原因查询
     * @return
     */
    @GetMapping("getReasonSelect")
    @NoRepeatSubmit
    @ApiOperation(value = "选择退款原因查询")
    public Result<List<CerePlatformDict>> getReasonSelect() throws CoBusinessException{
        List<String> dicts=cerePlatformDictService.getReasonSelect();
        return new Result(dicts,CoReturnFormat.SUCCESS);
    }

    @PostMapping("deleteAfter")
    @NoRepeatSubmit
    @ApiOperation(value = "删除售后单")
    public Result<Boolean> deleteAfter(@RequestBody CommonIdParam param) {
        cereOrderAfterService.deleteAfterById(param.getId());
        return new Result<>(true);
    }
}
