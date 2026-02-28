/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.seckill;

import com.shop.cereshop.app.page.buyer.MyProblem;
import com.shop.cereshop.app.page.seckill.SeckillAnswerDetail;
import com.shop.cereshop.app.page.seckill.SeckillProductProblem;
import com.shop.cereshop.commons.domain.tool.CereProductProblem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereProductProblemDAO extends BaseMapper<CereProductProblem> {
    int deleteByPrimaryKey(Long problemId);

    int insertSelective(CereProductProblem record);

    CereProductProblem selectByPrimaryKey(Long problemId);

    int updateByPrimaryKeySelective(CereProductProblem record);

    int updateByPrimaryKey(CereProductProblem record);

    List<SeckillProductProblem> getProblems(@Param("productId") Long productId);

    List<SeckillProductProblem> getSelfProblems(@Param("buyerUserId") Long buyerUserId);

    List<SeckillProductProblem> getSelfAnswers(@Param("buyerUserId") Long buyerUserId);

    List<Long> findOrderByUserProductId(@Param("productId") Long productId,@Param("buyerUserId") Long buyerUserId);

    SeckillAnswerDetail findProblemDetail(@Param("problemId") Long problemId);

    void updateSelected(@Param("ids") List<Long> ids);

    void deleteProblem(@Param("ids") List<Long> ids);

    List<MyProblem> getProblem(@Param("buyerUserId") Long buyerUserId);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);
}
