/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.seckill;

import com.shop.cereshop.app.page.seckill.PlatformSeckillProduct;
import com.shop.cereshop.app.param.seckill.PlatformSeckillParam;
import com.shop.cereshop.app.service.platformtool.CerePlatformSeckillService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformSeckill;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 平台秒杀专区
 */
@RestController
@RequestMapping("platform-seckill")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "PlatformSeckillController")
@Api(value = "平台秒杀专区", tags = "平台秒杀专区")
public class PlatformSeckillController {

    @Autowired
    private CerePlatformSeckillService cerePlatformSeckillService;

    /**
     * 平台秒杀查询今日场次
     * @return
     */
    @GetMapping("querySession")
    @ApiOperation(value = "平台秒杀查询今日场次")
    @Deprecated
    public Result<List<String>> querySession() throws CoBusinessException {
        try {
            List<String> sessionList = cerePlatformSeckillService.querySession();
            return new Result(sessionList, CoReturnFormat.SUCCESS);
        } catch (Exception e) {
            throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
        }
    }

    /**
     * 根据场次时间查询商品列表
     * @return
     */
    @PostMapping("queryProductListBySession")
    @ApiOperation(value = "根据场次时间查询商品列表")
    @Deprecated
    public Result<Page<PlatformSeckillProduct>> queryProductListBySession(@RequestBody PlatformSeckillParam platformSeckillParam) throws CoBusinessException {
        try {
            Page<PlatformSeckillProduct> pageList = cerePlatformSeckillService.queryProductListBySession(platformSeckillParam);
            return new Result(pageList, CoReturnFormat.SUCCESS);
        } catch (Exception e) {
            log.error("queryProductListBySession fail: session = {}", platformSeckillParam != null ? platformSeckillParam.getSession() : null, e);
            throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
        }
    }


    /**
     * 平台秒杀列表
     * @return
     */
    @GetMapping("queryPlatformSeckillList")
    @ApiOperation(value = "平台秒杀列表")
    @Deprecated
    public Result<List<CerePlatformSeckill>> queryPlatformSeckillList() throws CoBusinessException {
        try {
            List<CerePlatformSeckill> seckillList = cerePlatformSeckillService.queryPlatformSeckillList();
            return new Result(seckillList, CoReturnFormat.SUCCESS);
        } catch (Exception e) {
            throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
        }
    }
}
