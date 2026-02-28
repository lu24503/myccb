/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.product.impl;

import com.shop.cereshop.app.dao.product.CereProductImageDAO;
import com.shop.cereshop.app.service.product.CereProductImageService;
import com.shop.cereshop.commons.domain.product.CereProductImage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereProductImageServiceImpl implements CereProductImageService {

    CereProductImageDAO cereProductImageDAO;

    @Override
    public List<CereProductImage> selectByProductId(Long id) {
        return cereProductImageDAO.selectByProductId(id);
    }
}
