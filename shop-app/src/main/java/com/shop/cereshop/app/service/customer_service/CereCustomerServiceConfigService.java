/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.customer_service;

import com.shop.cereshop.commons.domain.customer_service.CereCustomerServiceConfig;

/**
 * <p>
 * 业务接口
 * 商家客服配置表
 * </p>
 *
 * @date 2021-12-22
 */
public interface CereCustomerServiceConfigService {

    CereCustomerServiceConfig getByShopId(Long shopId);

}
