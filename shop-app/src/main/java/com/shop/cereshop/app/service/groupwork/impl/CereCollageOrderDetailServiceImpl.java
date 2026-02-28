/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.groupwork.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shop.cereshop.app.dao.groupwork.CereCollageOrderDetailDAO;
import com.shop.cereshop.app.page.order.CollagePerson;
import com.shop.cereshop.app.service.groupwork.CereCollageOrderDetailService;
import com.shop.cereshop.commons.domain.collage.CereCollageOrderDetail;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereCollageOrderDetailServiceImpl implements CereCollageOrderDetailService {

    @Autowired
    private CereCollageOrderDetailDAO cereCollageOrderDetailDAO;

    @Override
    public void insert(CereCollageOrderDetail detail) throws CoBusinessException {
        cereCollageOrderDetailDAO.insert(detail);
    }

    @Override
    public void updateInvalid(List<Long> ids) throws CoBusinessException {
        cereCollageOrderDetailDAO.updateInvalid(ids);
    }

    @Override
    public CereCollageOrderDetail findKaituan(Long collageId) {
        return cereCollageOrderDetailDAO.findKaituan(collageId);
    }

    @Override
    public List<CollagePerson> findCollagePerson(Long collageId) {
        return cereCollageOrderDetailDAO.findCollagePerson(collageId);
    }

    @Override
    public List<CereCollageOrderDetail> findByOrderIdList(List<Long> orderIdList) {
        return cereCollageOrderDetailDAO.selectList(Wrappers.<CereCollageOrderDetail>lambdaQuery().in(CereCollageOrderDetail::getOrderId, orderIdList));
    }
}
