/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.after.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.cereshop.app.dao.after.CereAfterProductAttributeDAO;
import com.shop.cereshop.app.service.after.CereAfterProductAttributeService;
import com.shop.cereshop.commons.domain.after.CereAfterProduct;
import com.shop.cereshop.commons.domain.after.CereAfterProductAttribute;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereAfterProductAttributeServiceImpl implements CereAfterProductAttributeService {

    @Autowired
    private CereAfterProductAttributeDAO cereAfterProductAttributeDAO;

    @Override
    public void insertBatch(List<CereAfterProductAttribute> attributes) throws CoBusinessException {
        cereAfterProductAttributeDAO.insertBatch(attributes);
    }

}
