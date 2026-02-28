package com.shop.cereshop.app.controller.recommend;


import cn.hutool.core.util.ObjectUtil;
import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.page.recommend.*;
import com.shop.cereshop.app.param.recommend.*;
import com.shop.cereshop.app.service.recommend.CereRecommendCommentService;
import com.shop.cereshop.app.service.recommend.CereRecommendLikesService;
import com.shop.cereshop.app.service.recommend.CereRecommendTrendsService;
import com.shop.cereshop.app.service.recommend.CereRecommendTypeService;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.GsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 种草模块
 */
@RestController
@RequestMapping("recommend")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "RecommendController")
@Api(value = "种草模块", tags = "种草模块")
public class RecommendController {

    @Autowired
    private CereRecommendTypeService cereRecommendTypeService;

    @Autowired
    private CereRecommendTrendsService cereRecommendTrendsService;

    @Autowired
    private CereRecommendCommentService cereRecommendCommentService;

    @Autowired
    private CereRecommendLikesService cereRecommendLikesService;

    /**
     * 获取所有种草类型
     *
     * @return
     */
    @GetMapping(value = "/type/getAll")
    @ApiOperation(value = "获取所有种草类型")
    public Result<List<CereRecommendTypeVO>> getAllType() {
        return new Result(cereRecommendTypeService.getAll());
    }


    /**
     * 种草动态分页
     *
     * @return
     */
    @GetMapping(value = "/trend/page")
    @ApiOperation(value = "种草动态分页")
    public Result<Page<CereRecommendTrendPageVO>> trendPage(@Validated RecommendTrendPageParam param, HttpServletRequest request) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        param.setUserId(user.getBuyerUserId());
        return new Result(cereRecommendTrendsService.trendPage(param));
    }

    /**
     * 种草动态明细
     *
     * @return
     */
    @GetMapping(value = "/trend/get")
    @ApiOperation(value = "种草动态明细")
    public Result<CereRecommendTrendDetailVO> getTrend(@Validated Long recommendId, HttpServletRequest request) throws Exception {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        return new Result(cereRecommendTrendsService.getTrend(new RecommendLikesParam(recommendId, user.getBuyerUserId(), null)));
    }

    /**
     * 添加种草评论
     *
     * @return
     */
    @PostMapping(value = "/comment/add")
    @NoRepeatSubmit
    @ApiOperation(value = "添加种草评论")
    public Result<CereRecommendCommentPageVO> addComment(@Validated @RequestBody RecommendCommentSaveParam param, HttpServletRequest request) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        param.setUserId(user.getBuyerUserId());
        return new Result(cereRecommendCommentService.saveComment(param));
    }

    /**
     * 删除种草评论
     *
     * @return
     */
    @DeleteMapping(value = "/comment/delete")
    @NoRepeatSubmit
    @ApiOperation(value = "删除种草评论")
    public Result deleteComment(@Validated Long recommendCommentId, HttpServletRequest request) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereRecommendCommentService.deleteComment(recommendCommentId);
        return new Result(user.getName(), "删除种草评论", GsonUtil.objectToGson(recommendCommentId));
    }

    /**
     * 种草评论分页
     *
     * @return
     */
    @GetMapping(value = "/comment/page")
    @ApiOperation(value = "种草评论分页")
    public Result<Page<CereRecommendCommentPageVO>> commentPage(@Validated RecommendCommentPageParam param, HttpServletRequest request) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        param.setUserId(user.getBuyerUserId());
        Page<CereRecommendCommentPageVO> page = cereRecommendCommentService.commentPage(param);
        if (ObjectUtil.isNotNull(param.getReplyCommentId())) {
            CereRecommendCommentPageVO top = cereRecommendCommentService.commentTop(param);
            if (ObjectUtil.isNotNull(top)) {
                page.getList().add(0, top);
                return new Result(page, "reply-page");
            }
        }
        return new Result(page);
    }

    /**
     * 点赞
     *
     * @return
     */
    @PostMapping(value = "/likes/like")
    @NoRepeatSubmit
    @ApiOperation(value = "点赞")
    public Result commentPage(@Validated @RequestBody RecommendLikesParam param, HttpServletRequest request) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        param.setUserId(user.getBuyerUserId());
        cereRecommendLikesService.like(param);
        return new Result(user.getName(), "点赞", GsonUtil.objectToGson(param));
    }

    /**
     * 获取当前用户未读数
     *
     * @return
     */
    @GetMapping(value = "/comment/get-unread-count")
    @ApiOperation(value = "获取当前用户未读数")
    public Result<Integer> getUnreadCount(HttpServletRequest request) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        return new Result(cereRecommendCommentService.getUnreadCount(user.getBuyerUserId()));
    }

    /**
     * 我的消息
     *
     * @return
     */
    @GetMapping(value = "/comment/my-message")
    @ApiOperation(value = "我的消息")
    public Result<Page<MyCommentPageVO>> myMessage(@Validated MyCommentPageParam param, HttpServletRequest request) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        param.setUserId(user.getBuyerUserId());
        return new Result(cereRecommendCommentService.myMessage(param));
    }


    /**
     * 分享
     *
     * @return
     */
    @GetMapping(value = "/trend/share")
    @ApiOperation(value = "分享")
    public Result share(@Validated Long recommendId, HttpServletRequest request) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        cereRecommendTrendsService.share(recommendId);
        return new Result(user.getName(), "分享", GsonUtil.objectToGson(recommendId));
    }

    /**
     * 获取店铺信息
     *
     * @return
     */
    @GetMapping(value = "/trend/shop-info")
    @ApiOperation(value = "获取店铺信息")
    public Result<BusinessShopInfo> getShopInfo(@Validated Long shopId, HttpServletRequest request) {
        //获取当前登录账户
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        return new Result(cereRecommendTrendsService.getShopInfo(shopId, user.getBuyerUserId()));
    }

    /**
     * 获取评论状态
     *
     * @return
     */
    @GetMapping(value = "/comment/status")
    @ApiOperation(value = "获取评论状态")
    public Result<Boolean> getCommentStatus(@Validated Long recommendCommentId) {
        return new Result(cereRecommendCommentService.getCommentStatus(recommendCommentId));
    }
}
