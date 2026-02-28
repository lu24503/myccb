/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.product;

import com.shop.cereshop.app.page.classify.Classify;
import com.shop.cereshop.app.page.index.ProductClassify;
import com.shop.cereshop.app.param.classify.ClassifyParam;
import com.shop.cereshop.app.param.classify.ClassifyProductParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereProductClassifyService {

    List<ProductClassify> findAll();

    List<Classify> getFirstClassify(ClassifyParam param) throws CoBusinessException;

    Page getClassifyProducts(ClassifyProductParam param, CereBuyerUser user) throws CoBusinessException;

    Page getClassifyProducts2(ClassifyProductParam param, CereBuyerUser user);
}

