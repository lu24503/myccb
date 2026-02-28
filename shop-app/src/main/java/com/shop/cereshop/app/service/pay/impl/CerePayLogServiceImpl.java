/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.pay.impl;

import com.shop.cereshop.app.dao.pay.CerePayLogDAO;
import com.shop.cereshop.app.service.pay.CerePayLogService;
import com.shop.cereshop.commons.domain.pay.CerePayLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CerePayLogServiceImpl implements CerePayLogService {

    @Autowired
    private CerePayLogDAO cerePayLogDAO;

    @Override
    public void insert(CerePayLog payLog) throws Exception {
        cerePayLogDAO.insert(payLog);
    }
}
