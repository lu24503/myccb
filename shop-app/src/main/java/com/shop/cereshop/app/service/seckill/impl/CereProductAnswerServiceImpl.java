/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.seckill.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.seckill.CereProductAnswerDAO;
import com.shop.cereshop.app.page.buyer.MyAnswer;
import com.shop.cereshop.app.page.seckill.SeckillProductAnswer;
import com.shop.cereshop.app.param.seckill.AnswerIdParam;
import com.shop.cereshop.app.service.seckill.CereProductAnswerService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.tool.CereProductAnswer;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CereProductAnswerServiceImpl implements CereProductAnswerService {

    @Autowired
    private CereProductAnswerDAO cereProductAnswerDAO;

    @Override
    public List<SeckillProductAnswer> findAnswersById(Long problemId) {
        return cereProductAnswerDAO.findAnswersById(problemId);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void addAnswer(CereProductAnswer answer, CereBuyerUser user) throws CoBusinessException {
        String time = TimeUtils.yyMMddHHmmss();
        answer.setCreateTime(time);
        answer.setBuyerUserId(user.getBuyerUserId());
        answer.setIfAnonymous(IntegerEnum.NO.getCode());
        answer.setSelected(IntegerEnum.NO.getCode());
        cereProductAnswerDAO.insert(answer);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void selectedAnswer(AnswerIdParam param, CereBuyerUser user) throws CoBusinessException {
        //批量选中
        cereProductAnswerDAO.updateSelected(param.getIds());
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void deleteAnswer(AnswerIdParam param, CereBuyerUser user) throws CoBusinessException {
        //批量删除
        cereProductAnswerDAO.deleteAnswer(param.getIds());
    }

    @Override
    public Page getAnswer(PageParam param, CereBuyerUser user) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<MyAnswer> list=cereProductAnswerDAO.getAnswer(user.getBuyerUserId());
        PageInfo<MyAnswer> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereProductAnswerDAO.updateBuyerData(buyerUserId,id);
    }

    @Override
    public void deleteByProblemIds(List<Long> ids) throws CoBusinessException {
        cereProductAnswerDAO.deleteByProblemIds(ids);
    }

    @Override
    public int findAnswerCount(Long buyerUserId) {
        return cereProductAnswerDAO.selectCount(Wrappers.<CereProductAnswer>lambdaQuery().eq(CereProductAnswer::getBuyerUserId, buyerUserId));
    }
}
