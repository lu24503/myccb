/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.compose;

import java.util.List;
import java.util.Map;

public interface CereComposeProductService {

    List<Long> selectByComposeId(Long composeId);

    /**
     * 根据商品id列表查询组合活动id列表
     * @param productIdList
     * @return
     */
    Map<Long, List<Long>> selectComposeIdListByProductIdList(List<Long> productIdList);
}
