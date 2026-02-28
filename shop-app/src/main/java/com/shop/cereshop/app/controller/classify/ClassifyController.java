/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.classify;

import com.shop.cereshop.app.page.classify.Classify;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.param.classify.ClassifyParam;
import com.shop.cereshop.app.param.classify.ClassifyProductParam;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.product.CereProductClassifyService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.EmptyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 分类模块
 */
@RestController
@RequestMapping("classify")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "ClassifyController")
@Api(value = "分类模块", tags = "分类模块")
public class ClassifyController {

    @Autowired
    private CereProductClassifyService cereProductClassifyService;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    /**
     * 分类商品查询
     *
     * @return
     */
    @GetMapping("getClaasifyProducts")
    @ApiOperation(value = "商品搜索查询")
    public Result<Page<Product>> getClaasifyProducts(ClassifyProductParam param, HttpServletRequest request) throws CoBusinessException {
        String token = request.getHeader("Authorization");
        CereBuyerUser user = null;
        if (!EmptyUtils.isEmpty(token)) {
            //根据token查询用户信息
            user = cereBuyerUserService.findByToken(token);
        }
        Page page = cereProductClassifyService.getClassifyProducts(param, user);
        return new Result(page, CoReturnFormat.SUCCESS);
    }

    /**
     * 分类商品查询新接口，如果调用有问题可以切换回getProducts接口
     *
     * @return
     */
    @GetMapping("getClassifyProducts2")
    @ApiOperation(value = "商品搜索查询新接口")
    public Result<Page<Product>> getClassifyProducts2(ClassifyProductParam param, HttpServletRequest request) throws CoBusinessException {
        String token = request.getHeader("Authorization");
        CereBuyerUser user = null;
        if (!EmptyUtils.isEmpty(token)) {
            //根据token查询用户信息
            user = cereBuyerUserService.findByToken(token);
        }
        Page page = cereProductClassifyService.getClassifyProducts2(param, user);
        return new Result(page, CoReturnFormat.SUCCESS);
    }

    /**
     * 查询所有一级类目
     *
     * @return
     */
    @GetMapping("getFirstClassify")
    @ApiOperation(value = "查询店铺分类")
    public Result<List<Classify>> getFirstClassify(ClassifyParam param) throws CoBusinessException {
        List<Classify> list = cereProductClassifyService.getFirstClassify(param);
        return new Result(list, CoReturnFormat.SUCCESS);
    }
}
