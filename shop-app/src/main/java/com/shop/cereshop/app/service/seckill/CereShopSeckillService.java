/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.seckill;

import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.page.seckill.SeckillAnswerDetail;
import com.shop.cereshop.app.page.seckill.SeckillProductProblem;
import com.shop.cereshop.app.page.seckill.SeckillkIndex;
import com.shop.cereshop.app.page.seckill.ShopSeckillDetail;
import com.shop.cereshop.app.param.product.ProductParam;
import com.shop.cereshop.app.param.renovation.RenovationParam;
import com.shop.cereshop.app.param.seckill.ProductProblemParam;
import com.shop.cereshop.app.param.seckill.SeckillParam;
import com.shop.cereshop.app.param.seckill.ShopBusinessSeckill;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.tool.CereShopSeckill;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereShopSeckillService {
    SeckillkIndex getIndex(SeckillParam param, CereBuyerUser user) throws CoBusinessException,Exception;

    @Deprecated
    ProductDetail getById(CereBuyerUser user, ProductParam param, ProductDetail detail) throws CoBusinessException,Exception;

    Page getProblems(ProductProblemParam param, CereBuyerUser user) throws CoBusinessException;

    CereShopSeckill findById(Long shopSeckillId);

    Long findByProductId(Long skuId);

    SeckillAnswerDetail getProblemDetail(Long problemId,CereBuyerUser user) throws CoBusinessException;

    List<ShopSeckillDetail> getSeckills(RenovationParam param);

    List<ShopBusinessSeckill> selectByShopIdList(List<Long> shopIdList);

    ProductDetail setActivityInfo(Long shopSeckillId, CereBuyerUser user, ProductParam param, ProductDetail detail);

    ProductDetail setActivityInfoForRealInfo(Long shopSeckillId, CereBuyerUser user, ProductParam param, ProductDetail detail);
}
