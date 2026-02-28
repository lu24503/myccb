/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.buyer;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.param.buyer.BankParam;
import com.shop.cereshop.app.service.buyer.CereBuyerBankService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerBank;
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
import java.util.List;

/**
 * 银行卡模块
 */
@RestController
@RequestMapping("bank")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "BuyerBankController")
@Api(value = "银行卡模块", tags = "银行卡模块")
public class BuyerBankController {

    @Autowired
    private CereBuyerBankService cereBuyerBankService;

    /**
     * 添加银行卡
     * @param bank
     * @return
     */
    @PostMapping("save")
    @NoRepeatSubmit
    @ApiOperation(value = "添加银行卡")
    @NoRepeatWebLog
    public Result save(@RequestBody CereBuyerBank bank, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerBankService.save(bank,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"添加银行卡", GsonUtil.objectToGson(bank));
    }

    /**
     * 修改银行卡
     * @param bank
     * @return
     */
    @RequestMapping(value = "update", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "修改银行卡")
    @NoRepeatWebLog
    public Result update(@RequestBody CereBuyerBank bank, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerBankService.update(bank,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"修改银行卡", GsonUtil.objectToGson(bank));
    }

    /**
     * 删除银行卡
     * @return
     */
    @RequestMapping(value = "delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "删除银行卡")
    @NoRepeatWebLog
    public Result delete(@RequestBody BankParam bank,HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerBankService.delete(bank.getBankId(),user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"删除银行卡", GsonUtil.objectToGson(bank));
    }

    /**
     * 修改银行卡查询
     * @param bank
     * @return
     */
    @GetMapping("getById")
    @ApiOperation(value = "修改银行卡查询")
    public Result<CereBuyerBank> getById(BankParam bank) throws CoBusinessException{
        CereBuyerBank cereBuyerBank=cereBuyerBankService.getById(bank.getBankId());
        return new Result(cereBuyerBank,CoReturnFormat.SUCCESS);
    }

    /**
     * 银行卡管理查询
     * @param param
     * @return
     */
    @GetMapping("getAll")
    @ApiOperation(value = "银行卡管理查询")
    public Result<Page<CereBuyerBank>> getAll(BankParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        param.setBuyerUserId(user.getBuyerUserId());
        Page page=cereBuyerBankService.getAll(param);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 选择银行卡查询
     * @return
     */
    @GetMapping("getSelect")
    @ApiOperation(value = "选择银行卡查询")
    public Result<List<CereBuyerBank>> getSelect(HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        List<CereBuyerBank> list=cereBuyerBankService.getSelect(user.getBuyerUserId());
        return new Result(list,CoReturnFormat.SUCCESS);
    }

}
