/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.seckill.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.seckill.CereProductProblemDAO;
import com.shop.cereshop.app.page.buyer.MyProblem;
import com.shop.cereshop.app.page.seckill.SeckillAnswerDetail;
import com.shop.cereshop.app.page.seckill.SeckillProductProblem;
import com.shop.cereshop.app.param.seckill.ProblemIdParam;
import com.shop.cereshop.app.service.seckill.CereProductAnswerService;
import com.shop.cereshop.app.service.seckill.CereProductProblemService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.tool.CereProductProblem;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CereProductProblemServiceImpl implements CereProductProblemService {

    @Autowired
    private CereProductProblemDAO cereProductProblemDAO;

    @Autowired
    private CereProductAnswerService cereProductAnswerService;

    @Override
    public List<SeckillProductProblem> getProblems(Long productId) {
        return cereProductProblemDAO.getProblems(productId);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void addProblem(CereProductProblem problem, CereBuyerUser user) throws CoBusinessException {
        String time = TimeUtils.yyMMddHHmmss();
        problem.setCreateTime(time);
        problem.setBuyerUserId(user.getBuyerUserId());
        problem.setSelected(IntegerEnum.NO.getCode());
        cereProductProblemDAO.insert(problem);
    }

    @Override
    public List<SeckillProductProblem> getSelfProblems(Long buyerUserId) throws CoBusinessException {
        return cereProductProblemDAO.getSelfProblems(buyerUserId);
    }

    @Override
    public List<SeckillProductProblem> getSelfAnswers(Long buyerUserId) throws CoBusinessException {
        return cereProductProblemDAO.getSelfAnswers(buyerUserId);
    }

    @Override
    public List<Long> findOrderByUserProductId(Long productId, Long buyerUserId) {
        return cereProductProblemDAO.findOrderByUserProductId(productId,buyerUserId);
    }

    @Override
    public SeckillAnswerDetail findProblemDetail(Long problemId) {
        return cereProductProblemDAO.findProblemDetail(problemId);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void selectedProblem(ProblemIdParam problem, CereBuyerUser user) throws CoBusinessException {
        //批量选中
        cereProductProblemDAO.updateSelected(problem.getIds());
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void deleteProblem(ProblemIdParam param, CereBuyerUser user) throws CoBusinessException {
        //批量删除
        cereProductProblemDAO.deleteProblem(param.getIds());
        //批量删除对应回答数据
        cereProductAnswerService.deleteByProblemIds(param.getIds());
    }

    @Override
    public Page getProblem(PageParam param, CereBuyerUser user) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<MyProblem> list=cereProductProblemDAO.getProblem(user.getBuyerUserId());
        PageInfo<MyProblem> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereProductProblemDAO.updateBuyerData(buyerUserId,id);
    }

    @Override
    public int findProblemCount(Long buyerUserId) {
        return cereProductProblemDAO.selectCount(Wrappers.<CereProductProblem>lambdaQuery().eq(CereProductProblem::getBuyerUserId, buyerUserId));
    }
}
