/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.compose.impl;

import com.shop.cereshop.app.dao.compose.CereComposeProductDAO;
import com.shop.cereshop.app.page.compose.ComposeProduct;
import com.shop.cereshop.app.service.compose.CereComposeProductService;
import com.shop.cereshop.commons.domain.tool.CereComposeProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class CereComposeProductServiceImpl implements CereComposeProductService {

    @Autowired
    private CereComposeProductDAO cereComposeProductDAO;

    @Override
    public List<Long> selectByComposeId(Long composeId) {
        return cereComposeProductDAO.selectByComposeId(composeId);
    }

    @Override
    public Map<Long, List<Long>> selectComposeIdListByProductIdList(List<Long> productIdList) {
        if (CollectionUtils.isEmpty(productIdList)) {
            return Collections.emptyMap();
        }
        List<CereComposeProduct> list = cereComposeProductDAO.selectComposeIdListByProductId(productIdList);
        Map<Long, List<Long>> map = new HashMap<>();
        for (CereComposeProduct item:list) {
            List<Long> idList = map.get(item.getProductId());
            if (idList == null) {
                idList = new ArrayList<>();
            }
            idList.add(item.getComposeId());
            map.put(item.getProductId(), idList);
        }
        return map;
    }
}
