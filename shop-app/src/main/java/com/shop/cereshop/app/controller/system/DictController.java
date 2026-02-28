/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.system;

import com.shop.cereshop.app.service.dict.CerePlatformDictService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.dict.CerePlatformDict;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺模块
 */
@RestController
@RequestMapping("dict")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "DictController")
@Api(value = "字典模块", tags = "字典模块")
public class DictController {

    @Autowired
    private CerePlatformDictService cerePlatformDictService;

    /**
     * 数据字典编辑查询
     * @return
     */
    @GetMapping(value = "getByName")
    @ApiOperation(value = "数据字典编辑查询")
    public Result<CerePlatformDict> getById(String name) throws CoBusinessException {
        CerePlatformDict dict = cerePlatformDictService.getByName(name);
        return new Result(dict, CoReturnFormat.SUCCESS);
    }

}
