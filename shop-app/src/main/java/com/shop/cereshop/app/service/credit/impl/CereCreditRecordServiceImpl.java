/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.credit.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.credit.CereCreditRecordDAO;
import com.shop.cereshop.app.service.credit.CereCreditRecordService;
import com.shop.cereshop.commons.domain.activity.CerePlatformActivity;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.credit.CereCreditRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业务实现类
 * 积分流水表
 * </p>
 *
 * @author JustArgo
 * @date 2021-12-04
 */
@Slf4j
@Service
public class CereCreditRecordServiceImpl implements CereCreditRecordService {

    @Autowired
    private CereCreditRecordDAO cereCreditRecordDAO;

    @Override
    public Page<CereCreditRecord> selectCreditRecord(Long buyerUserId, PageParam param) {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        LambdaQueryWrapper<CereCreditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CereCreditRecord::getBuyerUserId, buyerUserId);
        wrapper.orderByDesc(CereCreditRecord::getCreateTime);
        List<CereCreditRecord> list=cereCreditRecordDAO.selectList(wrapper);
        PageInfo<CereCreditRecord> pageInfo=new PageInfo<>(list);
        Page<CereCreditRecord> page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }
}
