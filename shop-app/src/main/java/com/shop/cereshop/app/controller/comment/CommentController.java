/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.comment;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.page.comment.Comment;
import com.shop.cereshop.app.page.comment.CommentCount;
import com.shop.cereshop.app.page.comment.WordComment;
import com.shop.cereshop.app.param.comment.CommentIdParam;
import com.shop.cereshop.app.param.comment.CommentParam;
import com.shop.cereshop.app.param.comment.CommentWorParam;
import com.shop.cereshop.app.param.comment.LikeParam;
import com.shop.cereshop.app.service.shop.CereShopCommentService;
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
 * 我的评价
 */
@RestController
@RequestMapping("comment")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "CommentController")
@Api(value = "我的评价", tags = "我的评价")
public class CommentController {

    @Autowired
    private CereShopCommentService cereShopCommentService;

    /**
     * 我的评价列表查询
     * @return
     */
    @GetMapping("getAll")
    @ApiOperation(value = "我的评价列表查询")
    public Result<CommentCount> getAll(CommentParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        CommentCount comment= cereShopCommentService.getAll(param,user);
        return new Result(comment,CoReturnFormat.SUCCESS);
    }

    /**
     * 我的评价列表查询-1.5
     * @return
     */
    @GetMapping("getCommentList")
    @ApiOperation(value = "我的评价列表查询")
    public Result<CommentCount> getCommentList(CommentParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        CommentCount comment= cereShopCommentService.getCommentList(param,user);
        return new Result(comment,CoReturnFormat.SUCCESS);
    }

    /**
     * 商品评价列表查询
     * @return
     */
    @GetMapping("getProductAll")
    @ApiOperation(value = "商品评价列表查询")
    public Result<Page<WordComment>> getProductAll(CommentWorParam param) throws CoBusinessException,Exception{
        Page page= cereShopCommentService.getProductAll(param);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 评论详情查询
     * @return
     */
    @GetMapping("getById")
    @ApiOperation(value = "评论详情查询")
    public Result<Comment> getById(CommentIdParam param) throws CoBusinessException{
        Comment comment= cereShopCommentService.getById(param.getCommentId());
        return new Result(comment,CoReturnFormat.SUCCESS);
    }

    /**
     * 点赞评论
     * @return
     */
    @PostMapping("like")
    @ApiOperation(value = "点赞")
    @NoRepeatSubmit
    @NoRepeatWebLog
    public Result like(@RequestBody LikeParam param, HttpServletRequest request) throws CoBusinessException{
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereShopCommentService.like(param,user);
        return new Result(CoReturnFormat.SUCCESS,user.getWechatName(),"点赞评论", GsonUtil.objectToGson(param));
    }
}
