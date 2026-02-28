/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.risk.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.cereshop.app.dao.risk.CereRiskBlackDAO;
import com.shop.cereshop.app.service.risk.CereRiskBlackService;
import com.shop.cereshop.commons.domain.risk.CereRiskBlack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业务实现类
 * 黑名单表
 * </p>
 *
 * @author JustArgo
 * @date 2021-12-06
 */
@Slf4j
@Service
public class CereRiskBlackServiceImpl implements CereRiskBlackService {

    @Autowired
    private CereRiskBlackDAO cereRiskBlackDAO;

    @Override
    public List<CereRiskBlack> getEnabled() {
        return cereRiskBlackDAO.getEnabled();
    }
}
