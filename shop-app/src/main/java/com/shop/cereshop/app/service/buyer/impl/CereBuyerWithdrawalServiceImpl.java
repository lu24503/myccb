/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer.impl;

import com.shop.cereshop.app.dao.buyer.CereBuyerWithdrawalDAO;
import com.shop.cereshop.app.service.buyer.CereBuyerWithdrawalService;
import com.shop.cereshop.app.service.distributor.CereShopDistributorService;
import com.shop.cereshop.app.service.log.CerePlatformLogService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.buyer.CereBuyerWithdrawal;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CereBuyerWithdrawalServiceImpl implements CereBuyerWithdrawalService {

    @Autowired
    private CereBuyerWithdrawalDAO cereBuyerWithdrawalDAO;

    @Autowired
    private CerePlatformLogService cerePlatformLogService;

    @Autowired
    private CereShopDistributorService cereShopDistributorService;

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void save(CereBuyerWithdrawal withdrawal, CereBuyerUser user) throws CoBusinessException {
        //根据手机号查询分销员数据
        String time = TimeUtils.yyMMddHHmmss();
        withdrawal.setApplyTime(time);
        withdrawal.setState(IntegerEnum.BUYER_WITHDRAWAL_STAY.getCode());
        withdrawal.setBuyerUserId(user.getBuyerUserId());
        cereBuyerWithdrawalDAO.insert(withdrawal);
        //新增日志
        cerePlatformLogService.addLog(user,"账户模块","客户端操作","发起提现申请",withdrawal.getWithdrawalId(),time);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereBuyerWithdrawalDAO.updateBuyerData(buyerUserId,id);
    }
}
