/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.groupwork;

import com.shop.cereshop.app.page.groupwork.GroupInvite;
import com.shop.cereshop.app.page.groupwork.GroupWorkIndex;
import com.shop.cereshop.app.page.groupwork.ShareQrcode;
import com.shop.cereshop.app.page.settlement.Settlement;
import com.shop.cereshop.app.param.groupwork.*;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.groupwork.CereShopGroupWorkService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.EmptyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 拼团专区
 */
@RestController
@RequestMapping("work")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "GroupWorkController")
@Api(value = "拼团专区", tags = "拼团专区")
public class GroupWorkController {

    @Autowired
    private CereShopGroupWorkService cereShopGroupWorkService;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    /**
     * 拼团专区首页数据查询
     * @return
     */
    @GetMapping("getIndex")
    @ApiOperation(value = "拼团专区首页数据查询")
    public Result<GroupWorkIndex> getIndex(GroupWorkParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        GroupWorkIndex index= cereShopGroupWorkService.getIndex(param,user);
        return new Result(index, CoReturnFormat.SUCCESS);
    }

    /**
     * 拼团单结算查询
     * @param param
     * @return
     */
    @PostMapping("getSettlement")
    @ApiOperation(value = "拼团单结算查询")
    public Result<Settlement> getSettlement(@RequestBody GroupWorkSettlementParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Settlement settlement=cereShopGroupWorkService.getSettlement(param,user);
        return new Result(settlement,CoReturnFormat.SUCCESS);
    }

    /**
     * 邀请好友拼单查询
     * @param param
     * @return
     */
    @PostMapping("getInvite")
    @ApiOperation(value = "邀请好友拼单查询")
    public Result<GroupInvite> getInvite(@RequestBody CollageIdParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        GroupInvite groupInvite=cereShopGroupWorkService.getInvite(param,user);
        return new Result(groupInvite,CoReturnFormat.SUCCESS);
    }

    /**
     * 获取二维码和小程序码
     * @return
     */
    @GetMapping("getShare")
    @ApiOperation(value = "获取二维码和小程序码")
    public Result<GroupInvite> getShare(ShareQrcodeParam param) throws CoBusinessException,Exception{
        ShareQrcode qrcode=cereShopGroupWorkService.getShare(param);
        return new Result(qrcode,CoReturnFormat.SUCCESS);
    }
}
