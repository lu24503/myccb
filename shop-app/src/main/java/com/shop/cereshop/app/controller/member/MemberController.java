/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.member;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.param.member.MemberLevelInfo;
import com.shop.cereshop.app.service.member.CereMemberSigninRecordService;
import com.shop.cereshop.app.service.member.CerePlatformMemberLevelService;
import com.shop.cereshop.app.service.member.CerePlatformMembershipService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.member.CereMemberSigninRecord;
import com.shop.cereshop.commons.domain.member.CerePlatformMemberLevel;
import com.shop.cereshop.commons.domain.member.CerePlatformMembership;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 会员中心模块
 */
@RestController
@RequestMapping("member")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "MemberController")
@Api(value = "会员中心模块", tags = "会员中心模块")
public class MemberController {

    @Autowired
    private CerePlatformMembershipService cerePlatformMembershipService;

    @Autowired
    private CerePlatformMemberLevelService cerePlatformMemberLevelService;

    @Autowired
    private CereMemberSigninRecordService cereMemberSigninRecordService;

    /**
     * 会员权益查询
     * @return
     */
    @GetMapping("getMemberByMemberLevelId/{memberLevelId}")
    @NoRepeatSubmit
    @ApiOperation(value = "根据会员id查询会员信息")
    public Result<CerePlatformMemberLevel> getMemberByMemberLevelId(@PathVariable Long memberLevelId) throws CoBusinessException {
        try {
            CerePlatformMemberLevel level = cerePlatformMemberLevelService.selectByMemberLevelId(memberLevelId);
            return new Result(level, CoReturnFormat.SUCCESS);
        } catch(Exception e) {
            log.error("getMemberByMemberLevelId fail: memberLevelId = {}", memberLevelId);
            throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
        }
    }

    /**
     * 查询所有的会员等级详情
     * @return
     */
    @GetMapping("getAllMemberLevelInfo")
    @NoRepeatSubmit
    @ApiOperation(value = "查询所有的会员等级详情")
    public Result<List<MemberLevelInfo>> getAllMemberLevelInfo() throws CoBusinessException {
        try {
            List<MemberLevelInfo> levelInfoList = cerePlatformMemberLevelService.getAllMemberLevelInfo();
            return new Result(levelInfoList, CoReturnFormat.SUCCESS);
        } catch(Exception e) {
            throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
        }
    }

    /**
     * 会员权益查询
     * @return
     */
    @GetMapping("getMemberShipList")
    @NoRepeatSubmit
    @ApiOperation(value = "会员权益查询")
    public Result<List<CerePlatformMembership>> getUser(HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        List<CerePlatformMembership> membershipList=cerePlatformMembershipService.selectByMemberLevelId(user.getMemberLevelId());
        return new Result(membershipList, CoReturnFormat.SUCCESS);
    }

    /**
     * 查询签到列表
     * @return
     */
    @GetMapping("selectSigninRecordList")
    @NoRepeatSubmit
    @ApiOperation(value = "查询签到列表")
    public Result<List<CereMemberSigninRecord>> selectSigninRecordList(HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        List<CereMemberSigninRecord> signinRecordList=cereMemberSigninRecordService.selectSigninRecordList(user.getBuyerUserId());
        return new Result(signinRecordList, CoReturnFormat.SUCCESS);
    }

    /**
     * 查询签到明细
     * @return
     */
    @GetMapping("selectSigninHistory")
    @NoRepeatSubmit
    @ApiOperation(value = "查询签到明细")
    public Result<Page> selectSigninHistory(PageParam pageParam, HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page page = cereMemberSigninRecordService.selectSigninHistory(pageParam, user.getBuyerUserId());
        return new Result(page, CoReturnFormat.SUCCESS);
    }

    /**
     * 签到
     * @return
     */
    @PostMapping("signIn")
    @NoRepeatSubmit
    @ApiOperation(value = "签到")
    public Result<Boolean> signIn(HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        boolean result = cereMemberSigninRecordService.signIn(user.getBuyerUserId());
        return new Result(result, CoReturnFormat.SUCCESS);
    }

}
