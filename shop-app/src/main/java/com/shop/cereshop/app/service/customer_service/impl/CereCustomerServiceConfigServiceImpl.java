/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.customer_service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.cereshop.app.dao.customer_service.CereCustomerServiceConfigDAO;
import com.shop.cereshop.app.service.customer_service.CereCustomerServiceConfigService;
import com.shop.cereshop.commons.domain.customer_service.CereCustomerServiceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务实现类
 * 商家客服配置表
 * </p>
 *
 * @date 2021-12-22
 */
@Slf4j
@Service
public class CereCustomerServiceConfigServiceImpl implements CereCustomerServiceConfigService {

    @Autowired
    private CereCustomerServiceConfigDAO cereCustomerServiceConfigDAO;

    @Override
    public CereCustomerServiceConfig getByShopId(Long shopId) {
        LambdaQueryWrapper<CereCustomerServiceConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CereCustomerServiceConfig::getShopId, shopId);
        return cereCustomerServiceConfigDAO.selectOne(wrapper);
    }

}
