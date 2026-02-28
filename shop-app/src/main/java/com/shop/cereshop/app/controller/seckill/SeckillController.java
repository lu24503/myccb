/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.seckill;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.page.buyer.MyAnswer;
import com.shop.cereshop.app.page.buyer.MyProblem;
import com.shop.cereshop.app.page.seckill.SeckillAnswerDetail;
import com.shop.cereshop.app.page.seckill.SeckillProductProblem;
import com.shop.cereshop.app.page.seckill.SeckillkIndex;
import com.shop.cereshop.app.param.seckill.AnswerIdParam;
import com.shop.cereshop.app.param.seckill.ProblemIdParam;
import com.shop.cereshop.app.param.seckill.ProductProblemParam;
import com.shop.cereshop.app.param.seckill.SeckillParam;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.seckill.CereProductAnswerService;
import com.shop.cereshop.app.service.seckill.CereProductProblemService;
import com.shop.cereshop.app.service.seckill.CereShopSeckillService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.tool.CereProductAnswer;
import com.shop.cereshop.commons.domain.tool.CereProductProblem;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.GsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 秒杀专区
 */
@RestController
@RequestMapping("seckill")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "SeckillController")
@Api(value = "秒杀专区", tags = "秒杀专区")
public class SeckillController {

    @Autowired
    private CereShopSeckillService cereShopSeckillService;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    @Autowired
    private CereProductProblemService cereProductProblemService;

    @Autowired
    private CereProductAnswerService cereProductAnswerService;

    /**
     * 秒杀专区首页数据查询
     * @return
     */
    @GetMapping("getIndex")
    @ApiOperation(value = "秒杀专区首页数据查询")
    public Result<SeckillkIndex> getIndex(SeckillParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        //获取当前登录账户
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        SeckillkIndex index= cereShopSeckillService.getIndex(param,user);
        return new Result(index, CoReturnFormat.SUCCESS);
    }

//    /**
//     * 秒杀商品详情查询
//     * @param param
//     * @return
//     */
//    @GetMapping("getById")
//    @ApiOperation(value = "秒杀商品详情查询")
//    public Result<SeckillProductDetail> getById(SeckillProductParam param, HttpServletRequest request) throws CoBusinessException,Exception{
//        String token = request.getHeader("Authorization");
//        CereBuyerUser user=null;
//        if(!EmptyUtils.isEmpty(token)){
//            //根据token查询用户信息
//            user=cereBuyerUserService.findByToken(token);
//        }
//        SeckillProductDetail detail=cereShopSeckillService.getById(param,user);
//        return new Result(detail);
//    }

    /**
     * 商品问答数据查询
     * @return
     */
    @GetMapping("getProblems")
    @ApiOperation(value = "商品问答数据查询")
    public Result<Page<SeckillProductProblem>> getProblems(ProductProblemParam param,HttpServletRequest request) throws CoBusinessException,Exception{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        Page page= cereShopSeckillService.getProblems(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
         * 我的提问查询
     * @return
     */
    @GetMapping("getProblem")
    @ApiOperation(value = "我的提问查询")
    public Result<Page<MyProblem>> getProblem(PageParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        Page page= cereProductProblemService.getProblem(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 我的回答查询
     * @return
     */
    @GetMapping("getAnswer")
    @ApiOperation(value = "我的回答查询")
    public Result<Page<MyAnswer>> getAnswer(PageParam param, HttpServletRequest request) throws CoBusinessException,Exception{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        Page page= cereProductAnswerService.getAnswer(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 问答详情查询
     * @return
     */
    @GetMapping("getProblemDetail")
    @ApiOperation(value = "问答详情查询")
    public Result<SeckillAnswerDetail> getProblemDetail(ProductProblemParam param, HttpServletRequest request) throws CoBusinessException{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        SeckillAnswerDetail detail= cereShopSeckillService.getProblemDetail(param.getProblemId(),user);
        return new Result(detail,CoReturnFormat.SUCCESS);
    }

    /**
     * 提问
     * @param problem
     * @return
     */
    @PostMapping("addProblem")
    @NoRepeatSubmit
    @ApiOperation(value = "提问")
    @NoRepeatWebLog
    public Result addProblem(@RequestBody CereProductProblem problem, HttpServletRequest request)  throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereProductProblemService.addProblem(problem,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"提问", GsonUtil.objectToGson(problem));
    }

    /**
     * 选中提问
     * @param param
     * @return
     */
    @RequestMapping(value = "selectedProblem", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "选中提问")
    @NoRepeatWebLog
    public Result selectedProblem(@RequestBody ProblemIdParam param, HttpServletRequest request)  throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereProductProblemService.selectedProblem(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"选中提问", GsonUtil.objectToGson(param));
    }

    /**
     * 批量删除提问
     * @param param
     * @return
     */
    @RequestMapping(value = "deleteProblem", method = {RequestMethod.DELETE, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "批量删除提问")
    @NoRepeatWebLog
    public Result deleteProblem(@RequestBody ProblemIdParam param, HttpServletRequest request)  throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereProductProblemService.deleteProblem(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"批量删除提问", GsonUtil.objectToGson(param));
    }

    /**
     * 选中回答
     * @param param
     * @return
     */
    @RequestMapping(value = "selectedAnswer", method = {RequestMethod.PUT, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "选中回答")
    @NoRepeatWebLog
    public Result selectedAnswer(@RequestBody AnswerIdParam param, HttpServletRequest request)  throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereProductAnswerService.selectedAnswer(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"选中回答", GsonUtil.objectToGson(param));
    }

    /**
     * 批量删除回答
     * @param param
     * @return
     */
    @RequestMapping(value = "deleteAnswer", method = {RequestMethod.DELETE, RequestMethod.POST})
    @NoRepeatSubmit
    @ApiOperation(value = "批量删除回答")
    @NoRepeatWebLog
    public Result deleteAnswer(@RequestBody AnswerIdParam param, HttpServletRequest request)  throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereProductAnswerService.deleteAnswer(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"批量删除回答", GsonUtil.objectToGson(param));
    }

    /**
     * 回答
     * @param answer
     * @return
     */
    @PostMapping("addAnswer")
    @NoRepeatSubmit
    @ApiOperation(value = "回答")
    @NoRepeatWebLog
    public Result addAnswer(@RequestBody CereProductAnswer answer, HttpServletRequest request)  throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereProductAnswerService.addAnswer(answer,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"回答", GsonUtil.objectToGson(answer));
    }
}
