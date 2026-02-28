/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.buyer;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.param.buyer.ReceiveParam;
import com.shop.cereshop.app.service.buyer.CereBuyerReceiveService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerReceive;
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
import java.util.List;

/**
 * 收货地址模块
 */
@RestController
@RequestMapping("receive")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "BuyerReceiveController")
@Api(value = "收货地址模块", tags = "收货地址模块")
public class BuyerReceiveController {

    @Autowired
    private CereBuyerReceiveService cereBuyerReceiveService;

    /**
     * 添加收货地址
     * @param receive
     * @return
     */
    @PostMapping("save")
    @NoRepeatSubmit
    @ApiOperation(value = "添加收货地址")
    @NoRepeatWebLog
    public Result<CereBuyerReceive> addCart(@RequestBody CereBuyerReceive receive, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        CereBuyerReceive cereBuyerReceive=cereBuyerReceiveService.save(receive,user);
        return new Result(cereBuyerReceive,CoReturnFormat.SUCCESS,user.getWechatName(),"添加收货地址", GsonUtil.objectToGson(receive));
    }

    /**
     * 修改收货地址
     * @param receive
     * @return
     */
    @RequestMapping(value = "update", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "修改收货地址")
    @NoRepeatWebLog
    public Result update(@RequestBody CereBuyerReceive receive, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerReceiveService.update(receive,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"修改收货地址", GsonUtil.objectToGson(receive));
    }

    /**
     * 删除收货地址
     * @param param
     * @return
     */
    @RequestMapping(value = "delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "删除收货地址")
    @NoRepeatWebLog
    public Result delete(@RequestBody ReceiveParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerReceiveService.delete(param.getReceiveId(),user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"删除收货地址", GsonUtil.objectToGson(param));
    }

    /**
     * 修改收货地址查询
     * @param param
     * @return
     */
    @GetMapping("getById")
    @ApiOperation(value = "修改收货地址查询")
    public Result<CereBuyerReceive> getById(ReceiveParam param) throws CoBusinessException{
        CereBuyerReceive receive=cereBuyerReceiveService.getById(param.getReceiveId());
        return new Result(receive,CoReturnFormat.SUCCESS);
    }

    /**
     * 收货地址管理查询
     * @param param
     * @return
     */
    @GetMapping("getAll")
    @ApiOperation(value = "收货地址列表查询")
    public Result<Page<CereBuyerReceive>> getAll(PageParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page page=cereBuyerReceiveService.getAll(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 选择收货地址查询
     * @return
     */
    @GetMapping("getSelect")
    @ApiOperation(value = "选择收货地址查询")
    public Result<List<CereBuyerReceive>> getSelect(HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        List<CereBuyerReceive> list=cereBuyerReceiveService.getSelect(user.getBuyerUserId());
        return new Result(list,CoReturnFormat.SUCCESS);
    }
}
