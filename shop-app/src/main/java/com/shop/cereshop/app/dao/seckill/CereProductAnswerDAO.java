/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.seckill;

import com.shop.cereshop.app.page.buyer.MyAnswer;
import com.shop.cereshop.app.page.seckill.SeckillProductAnswer;
import com.shop.cereshop.commons.domain.tool.CereProductAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereProductAnswerDAO extends BaseMapper<CereProductAnswer> {
    int deleteByPrimaryKey(Long answerId);

    int insertSelective(CereProductAnswer record);

    CereProductAnswer selectByPrimaryKey(Long answerId);

    int updateByPrimaryKeySelective(CereProductAnswer record);

    int updateByPrimaryKey(CereProductAnswer record);

    List<SeckillProductAnswer> findAnswersById(@Param("problemId") Long problemId);

    void updateSelected(@Param("ids") List<Long> ids);

    void deleteAnswer(@Param("ids") List<Long> ids);

    List<MyAnswer> getAnswer(@Param("buyerUserId") Long buyerUserId);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);

    void deleteByProblemIds(@Param("ids") List<Long> ids);
}
