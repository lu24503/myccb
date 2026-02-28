/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.after.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.cereshop.app.dao.after.CereAfterDileverDAO;
import com.shop.cereshop.app.service.after.CereAfterDileverService;
import com.shop.cereshop.commons.domain.after.CereAfterDilever;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CereAfterDileverServiceImpl implements CereAfterDileverService {

    @Autowired
    private CereAfterDileverDAO cereAfterDileverDAO;

    @Override
    public void insert(CereAfterDilever dilever) throws CoBusinessException {
        cereAfterDileverDAO.insert(dilever);
    }

    @Override
    public void deleteByAfterId(Long id) {
        LambdaQueryWrapper<CereAfterDilever> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CereAfterDilever::getAfterId, id);
        cereAfterDileverDAO.delete(wrapper);
    }
}
