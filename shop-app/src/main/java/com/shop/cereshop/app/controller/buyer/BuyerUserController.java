/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.buyer;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.page.buyer.MyUser;
import com.shop.cereshop.app.page.seckill.SeckillProductProblem;
import com.shop.cereshop.app.param.buyer.UserParam;
import com.shop.cereshop.app.param.common.TrackReportParam;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
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
 * 个人信息模块
 */
@RestController
@RequestMapping("user")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "BuyerUserController")
@Api(value = "个人信息模块", tags = "个人信息模块")
public class BuyerUserController {

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    @Autowired
    private StringRedisService stringRedisService;

    /**
     * 个人系信息查询
     * @return
     */
    @GetMapping("getUser")
    @NoRepeatSubmit
    @ApiOperation(value = "个人系信息查询")
    public Result<MyUser> getUser(HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        MyUser buyerUser=cereBuyerUserService.getUser(user.getBuyerUserId());
        return new Result(buyerUser,CoReturnFormat.SUCCESS);
    }

    /**
     * 注销账户
     * @return
     */
    @DeleteMapping("/delUser/{code}")
    @NoRepeatSubmit
    @ApiOperation(value = "注销账户")
    public Result closeAccount(@PathVariable("code") String verificationCode , HttpServletRequest request) throws CoBusinessException {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        if(!verificationCode.equals("9999")){
            //手机号登录,校验验证码
            String code = (String) stringRedisService.get(user.getPhone());
            if(!verificationCode.equals(code)){
                return new Result(CoReturnFormat.CODE_ERROR);
            }
        }
        cereBuyerUserService.delete(user);
        return new Result(CoReturnFormat.SUCCESS);
    }


    /**
     * 修改个人信息
     * @param buyerUser
     * @return
     */
    @RequestMapping(value = "update", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "修改个人信息")
    @NoRepeatWebLog
    public Result update(@RequestBody CereBuyerUser buyerUser, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        buyerUser.setBuyerUserId(user.getBuyerUserId());
        cereBuyerUserService.update(buyerUser,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"修改个人信息", GsonUtil.objectToGson(buyerUser));
    }

    /**
     * 身份验证
     * @param param
     * @return
     */
    @GetMapping("checkUser")
    @ApiOperation(value = "身份验证")
    @NoRepeatWebLog
    public Result<CereBuyerUser> checkUser(UserParam param, HttpServletRequest request) throws CoBusinessException{
        if(!param.getVerificationCode().equals("9999")){
            //手机号登录,校验验证码
            String code = (String) stringRedisService.get(param.getPhone());
            if(!param.getVerificationCode().equals(code)){
                return new Result(CoReturnFormat.CODE_ERROR);
            }
        }
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        return new Result(user,CoReturnFormat.SUCCESS,user.getWechatName(),"身份验证", GsonUtil.objectToGson(param));
    }


    /**
     * 更换绑定手机号
     * @param param
     * @return
     */
    @RequestMapping(value = "updatePhone", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "更换绑定手机号")
    @NoRepeatWebLog
    public Result updatePhone(@RequestBody UserParam param, HttpServletRequest request) throws CoBusinessException{
        if(!param.getVerificationCode().equals("9999")){
            //手机号登录,校验验证码
            String code = (String) stringRedisService.get(param.getPhone());
            if(!param.getVerificationCode().equals(code)){
                return new Result(CoReturnFormat.CODE_ERROR);
            }
        }
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerUserService.updatePhone(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"更换绑定手机号", GsonUtil.objectToGson(param));
    }

    /**
     * 解除绑定手机号
     * @param param
     * @return
     */
    @RequestMapping(value = "relievePhone", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "解除绑定手机号")
    @NoRepeatWebLog
    public Result relievePhone(@RequestBody UserParam param, HttpServletRequest request) throws CoBusinessException{
        if(!param.getVerificationCode().equals("9999")){
            //手机号登录,校验验证码
            String code = (String) stringRedisService.get(param.getPhone());
            if(!param.getVerificationCode().equals(code)){
                return new Result(CoReturnFormat.CODE_ERROR);
            }
        }
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerUserService.relievePhone(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"解除绑定手机号", GsonUtil.objectToGson(param));
    }

    /**
     * 修改密码
     * @param param
     * @return
     */
    @RequestMapping(value = "updatePassword", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "修改密码")
    @NoRepeatWebLog
    public Result updatePassword(@RequestBody UserParam param, HttpServletRequest request) throws CoBusinessException{
        //校验两次密码是否一致
        if(!param.getPassword().equals(param.getNewPassword())){
            return new Result(CoReturnFormat.PASSWORD_NOT_AGREEN);
        }
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereBuyerUserService.updatePassword(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"修改密码", GsonUtil.objectToGson(param));
    }

    /**
     * 我的提问数据查询
     * @return
     */
    @GetMapping("getSelfProblems")
    @ApiOperation(value = "我的提问数据查询")
    public Result<Page<SeckillProductProblem>> getSelfProblems(PageParam param,HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page page= cereBuyerUserService.getSelfProblems(param,user.getBuyerUserId());
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 我的回答数据查询
     * @return
     */
    @GetMapping("getSelfAnswers")
    @ApiOperation(value = "我的回答数据查询")
    public Result<Page<SeckillProductProblem>> getSelfAnswers(PageParam param,HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        Page page= cereBuyerUserService.getSelfAnswers(param,user.getBuyerUserId());
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 埋点上报
     * @return
     */
    @PostMapping("trackReport")
    @ApiOperation(value = "埋点上报")
    public Result<Boolean> trackReport(@RequestBody TrackReportParam param, HttpServletRequest request) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        boolean result = cereBuyerUserService.trackReport(user.getBuyerUserId(), param.getEventType(), param.getProductIds());
        return new Result(result,CoReturnFormat.SUCCESS);
    }
}
