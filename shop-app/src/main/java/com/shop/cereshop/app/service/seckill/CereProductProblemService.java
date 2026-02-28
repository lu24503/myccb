/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.seckill;

import com.shop.cereshop.app.page.seckill.SeckillAnswerDetail;
import com.shop.cereshop.app.page.seckill.SeckillProductProblem;
import com.shop.cereshop.app.param.seckill.ProblemIdParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.tool.CereProductProblem;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereProductProblemService {
    List<SeckillProductProblem> getProblems(Long productId);

    void addProblem(CereProductProblem problem, CereBuyerUser user) throws CoBusinessException;

    List<SeckillProductProblem> getSelfProblems(Long buyerUserId) throws CoBusinessException;

    List<SeckillProductProblem> getSelfAnswers(Long buyerUserId) throws CoBusinessException;

    List<Long> findOrderByUserProductId(Long productId, Long buyerUserId);

    SeckillAnswerDetail findProblemDetail(Long problemId);

    void selectedProblem(ProblemIdParam problem, CereBuyerUser user) throws CoBusinessException;

    void deleteProblem(ProblemIdParam param, CereBuyerUser user) throws CoBusinessException;

    Page getProblem(PageParam param, CereBuyerUser user) throws CoBusinessException;

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;

    int findProblemCount(Long buyerUserId);
}
