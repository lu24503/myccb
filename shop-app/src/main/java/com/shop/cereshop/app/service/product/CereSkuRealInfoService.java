/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.product;

import com.shop.cereshop.app.param.product.RefreshRealInfoDTO;

/**
 * <p>
 * sku当前销量和当前活动信息更新服务
 *
 * </p>
 *
 * @author
 * @date 2022-07-17
 */
public interface CereSkuRealInfoService {

    void refreshSkuRealInfo(RefreshRealInfoDTO refreshRealInfoDTO);

    boolean refreshAllProductRealInfo();

    void refreshSkuRealInfoForActivity(RefreshRealInfoDTO RefreshRealInfoDTO);
}
