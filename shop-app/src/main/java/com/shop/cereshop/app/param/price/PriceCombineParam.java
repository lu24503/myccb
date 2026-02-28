/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.price;

import com.shop.cereshop.commons.domain.tool.CerePriceRule;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 定价捆绑销售活动组合信息
 */
@Data
public class PriceCombineParam {

    /**
     * 活动id 和 商品id的对应关系
     */
    Map<Long, List<Long>> priceProductIdListMap;

    /**
     * 店铺id - 活动id - 活动配置
     */
    Map<Long,Map<Long,List<CerePriceRule>>> priceMap;

}
