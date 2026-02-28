/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.credit;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.page.coupon.CreditCoupon;
import com.shop.cereshop.app.param.coupon.CouponParam;
import com.shop.cereshop.app.param.credit.CreditSignInParam;
import com.shop.cereshop.app.service.activity.CereBuyerCouponService;
import com.shop.cereshop.app.service.activity.CerePlatformActivityService;
import com.shop.cereshop.app.service.credit.CereCreditRecordService;
import com.shop.cereshop.app.service.credit.CereCreditSigninRecordService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.activity.CerePlatformActivity;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.credit.CereCreditRecord;
import com.shop.cereshop.commons.domain.credit.CereCreditSigninRecord;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 积分模块
 */
@RestController
@RequestMapping("credit")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "CreditController")
@Api(value = "积分模块", tags = "积分模块")
public class CreditController {

    @Autowired
    private CereCreditSigninRecordService cereCreditSigninRecordService;

    @Autowired
    private CereCreditRecordService cereCreditRecordService;

    @Autowired
    private CerePlatformActivityService cerePlatformActivityService;

    @Autowired
    private CereBuyerCouponService cereBuyerCouponService;

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
        boolean result = cereCreditSigninRecordService.signIn(user.getBuyerUserId());
        return new Result(result, CoReturnFormat.SUCCESS);
    }

    /**
     * 根据月份查询签到记录
     * @return
     */
    @PostMapping("selectByMonth")
    @NoRepeatSubmit
    @ApiOperation(value = "根据月份查询签到记录")
    public Result<List<CereCreditSigninRecord>> selectByMonth(@RequestBody CreditSignInParam param, HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        List<CereCreditSigninRecord> result = cereCreditSigninRecordService.selectByMonth(user.getBuyerUserId(), param.getMonth());
        return new Result(result, CoReturnFormat.SUCCESS);
    }

    /**
     * 查询积分记录
     * @return
     */
    @PostMapping("selectCreditRecord")
    @NoRepeatSubmit
    @ApiOperation(value = "查询积分记录")
    public Result<Page<CereCreditRecord>> selectCreditRecord(@RequestBody PageParam param, HttpServletRequest request) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page<CereCreditRecord> result = cereCreditRecordService.selectCreditRecord(user.getBuyerUserId(), param);
        return new Result(result, CoReturnFormat.SUCCESS);
    }

    /**
     * 查询可以积分兑换的优惠券
     * @return
     */
    @PostMapping("selectCreditCouponList")
    @NoRepeatSubmit
    @ApiOperation(value = "查询可以积分兑换的优惠券")
    public Result<Page<CreditCoupon>> selectCreditCouponList(@RequestBody PageParam param, HttpServletRequest request) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page<CreditCoupon> result = cerePlatformActivityService.selectCreditCouponList(user.getBuyerUserId(), param);
        return new Result(result, CoReturnFormat.SUCCESS);
    }

    /**
     * 积分兑换优惠券
     * @return
     */
    @PostMapping("exchangeCoupon")
    @NoRepeatSubmit
    @ApiOperation(value = "积分兑换优惠券")
    public Result<Boolean> exchangeCoupon(@RequestBody CouponParam param, HttpServletRequest request) throws Exception {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        param.setSource(IntegerEnum.COUPON_SOURCE_CREDIT_EXCHANGE.getCode());
        cereBuyerCouponService.takeCoupon(param,user);
        return new Result(true, CoReturnFormat.SUCCESS);
    }

}
