/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.seckill;

import com.shop.cereshop.app.page.seckill.SeckillProductAnswer;
import com.shop.cereshop.app.param.seckill.AnswerIdParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.tool.CereProductAnswer;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereProductAnswerService {
    List<SeckillProductAnswer> findAnswersById(Long problemId);

    void addAnswer(CereProductAnswer answer, CereBuyerUser user) throws CoBusinessException;

    void selectedAnswer(AnswerIdParam param, CereBuyerUser user) throws CoBusinessException;

    void deleteAnswer(AnswerIdParam param, CereBuyerUser user) throws CoBusinessException;

    Page getAnswer(PageParam param, CereBuyerUser user) throws CoBusinessException;

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;

    void deleteByProblemIds(List<Long> ids) throws CoBusinessException;

    int findAnswerCount(Long buyerUserId);
}
