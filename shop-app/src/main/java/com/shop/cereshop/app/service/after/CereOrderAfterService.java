/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.after;


import com.shop.cereshop.app.page.after.AfterDetail;
import com.shop.cereshop.app.param.after.AfterGetAllParam;
import com.shop.cereshop.app.param.after.AfterGetByIdParam;
import com.shop.cereshop.app.param.after.AfterParam;
import com.shop.cereshop.app.param.after.PlatformParam;
import com.shop.cereshop.commons.domain.after.CereOrderAfter;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.math.BigDecimal;
import java.util.List;

public interface CereOrderAfterService {

    void submit(AfterParam param, CereBuyerUser user) throws CoBusinessException,Exception;

    BigDecimal getReturnPrice(AfterParam param, CereBuyerUser user) throws Exception;

    Page getAll(AfterGetAllParam param, CereBuyerUser user) throws CoBusinessException;

    AfterDetail getById(Long afterId) throws CoBusinessException,Exception;

    void returnRefund(AfterGetByIdParam param, CereBuyerUser user) throws CoBusinessException,Exception;

    void returnGoods(AfterGetByIdParam param, CereBuyerUser user) throws CoBusinessException,Exception;

    void platform(PlatformParam param, CereBuyerUser user) throws CoBusinessException;

    void update(CereOrderAfter cereOrderAfter) throws CoBusinessException;

    Integer getPostSaleNumByMonth(Integer hours, Long buyerUserId);

    void deleteAfterById(Long id);

    List<Integer> selectAfterStateList(Long orderId);

    int findAfterCount(Long buyerUserId);

    List<CereOrderAfter> selectByOrderIdList(List<Long> orderIdList);
}
