/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.notice;

import com.shop.cereshop.app.page.notice.BuyerNotice;
import com.shop.cereshop.app.page.notice.NoticeDetail;
import com.shop.cereshop.app.param.notice.NoticeIdParam;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.notice.CereNoticeService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.notice.CereNotice;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.EmptyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 消息中心
 */
@RestController
@RequestMapping("notice")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "NoticeController")
@Api(value = "消息中心", tags = "消息中心")
public class NoticeController {

    @Autowired
    private CereNoticeService cereNoticeService;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    /**
     * 消息列表查询
     * @return
     */
    @GetMapping("getAll")
    @ApiOperation(value = "消息列表查询")
    public Result<Page<BuyerNotice>> getAll(PageParam pageParam,HttpServletRequest request) throws CoBusinessException{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        Page page=cereNoticeService.getAll(pageParam,user);
        return new Result(page, CoReturnFormat.SUCCESS);
    }

    /**
     * 公告消息列表查询
     * @return
     */
    @GetMapping("getGongGaoAll")
    @ApiOperation(value = "公告消息列表查询")
    public Result<List<BuyerNotice>> getGongGaoAll(HttpServletRequest request) throws CoBusinessException{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        List<BuyerNotice> list=cereNoticeService.getGongGaoAll(user);
        return new Result(list, CoReturnFormat.SUCCESS);
    }

    /**
     * 公告详情查询
     * @return
     */
    @GetMapping("getById")
    @ApiOperation(value = "公告详情查询")
    public Result<NoticeDetail> getById(NoticeIdParam param) throws CoBusinessException{
        NoticeDetail detail=cereNoticeService.getById(param);
        return new Result(detail,CoReturnFormat.SUCCESS);
    }

    @PostMapping("readNotice")
    @ApiOperation(value = "已读某个消息")
    public Result<Boolean> readNotice(@RequestBody NoticeIdParam param,HttpServletRequest request) throws CoBusinessException {
        CereBuyerUser cereBuyerUser = (CereBuyerUser)request.getAttribute("user");
        if (cereBuyerUser == null) {
            return new Result<>(false, CoReturnFormat.SUCCESS);
        }
        cereNoticeService.readNotice(param.getNoticeId(), cereBuyerUser.getBuyerUserId());
        return new Result<>(true, CoReturnFormat.SUCCESS);
    }

    @PostMapping("readAll")
    @ApiOperation(value = "全部已读")
    public Result<Boolean> readAll(HttpServletRequest request) throws CoBusinessException {
        CereBuyerUser cereBuyerUser = (CereBuyerUser)request.getAttribute("user");
        if (cereBuyerUser == null) {
            return new Result<>(false, CoReturnFormat.SUCCESS);
        }
        cereNoticeService.readAll(cereBuyerUser.getBuyerUserId());
        return new Result<>(true, CoReturnFormat.SUCCESS);
    }

    @PostMapping("removeById")
    @ApiOperation(value = "删除消息")
    public Result<Boolean> removeById(@RequestBody NoticeIdParam param, HttpServletRequest request) throws CoBusinessException {
        CereBuyerUser cereBuyerUser = (CereBuyerUser)request.getAttribute("user");
        if (cereBuyerUser == null) {
            return new Result<>(false, CoReturnFormat.SUCCESS);
        }
        cereNoticeService.removeById(param.getNoticeId(), cereBuyerUser.getBuyerUserId());
        return new Result<>(true, CoReturnFormat.SUCCESS);
    }
}
