/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.log.impl;

import com.shop.cereshop.app.dao.log.CerePlatformLogDAO;
import com.shop.cereshop.app.service.log.CerePlatformLogService;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.log.CerePlatformLog;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CerePlatformLogServiceImpl implements CerePlatformLogService {

    @Autowired
    private CerePlatformLogDAO cerePlatformLogDAO;

    @Override
    public void addLog(CereBuyerUser user, String module, String operation, String describe, Long id, String time) throws CoBusinessException {
        CerePlatformLog log=new CerePlatformLog();
        log.setPlatformUserId(user.getBuyerUserId());
        log.setModule(module);
        log.setOperation(operation);
        log.setOperationDescribtion(describe);
        log.setOnly(String.valueOf(id));
        log.setCreateTime(time);
        cerePlatformLogDAO.insert(log);
    }
}
