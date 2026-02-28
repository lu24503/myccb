/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.buyer.CereBuyerBankDAO;
import com.shop.cereshop.app.param.buyer.BankParam;
import com.shop.cereshop.app.service.buyer.CereBuyerBankService;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.domain.buyer.CereBuyerBank;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CereBuyerBankServiceImpl implements CereBuyerBankService {

    @Autowired
    private CereBuyerBankDAO cereBuyerBankDAO;

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void save(CereBuyerBank bank, CereBuyerUser user) throws CoBusinessException {
        //校验重复
        CereBuyerBank cereBuyerBank=cereBuyerBankDAO.checkBank(user.getBuyerUserId(),bank.getBankCard(),null);
        if(cereBuyerBank!=null){
            throw new CoBusinessException(CoReturnFormat.BANK_ALREADY);
        }
        bank.setBuyerUserId(user.getBuyerUserId());
        cereBuyerBankDAO.insert(bank);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void update(CereBuyerBank bank, CereBuyerUser user) throws CoBusinessException {
        //校验重复
        CereBuyerBank cereBuyerBank=cereBuyerBankDAO.checkBank(user.getBuyerUserId(),bank.getBankCard(),bank.getBankId());
        if(cereBuyerBank!=null){
            throw new CoBusinessException(CoReturnFormat.BANK_ALREADY);
        }
        cereBuyerBankDAO.updateById(bank);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void delete(Long bankId,CereBuyerUser user) throws CoBusinessException {
        cereBuyerBankDAO.deleteById(bankId);
    }

    @Override
    public CereBuyerBank getById(Long bankId) throws CoBusinessException {
        return cereBuyerBankDAO.getById(bankId);
    }

    @Override
    public Page<CereBuyerBank> getAll(BankParam param) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<CereBuyerBank> list=cereBuyerBankDAO.getAll(param.getBuyerUserId());
        //将银行卡前面部分替换成*
        list.stream()
                .filter(Objects::nonNull)
                .peek(a -> {
                    StringBuffer buffer=new StringBuffer();
                    String last= a.getBankCard().substring(a.getBankCard().length()-5);
                    for (int i = 0; i < a.getBankCard().length()-4; i++) {
                        buffer.append("*");
                    }
                    buffer.append(last);
                    a.setBankCard(last);
                });
        PageInfo<CereBuyerBank> pageInfo=new PageInfo<>(list);
        return new Page<>(pageInfo.getList(),pageInfo.getTotal());
    }

    @Override
    public List<CereBuyerBank> getSelect(Long buyerUserId) throws CoBusinessException {
        return cereBuyerBankDAO.getAll(buyerUserId);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereBuyerBankDAO.updateBuyerData(buyerUserId,id);
    }
}
