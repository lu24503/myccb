/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.seckill;

import com.shop.cereshop.commons.domain.common.PageParam;
import lombok.Data;

/**
 * 平台秒杀活动参数
 */
@Data
public class PlatformSeckillParam extends PageParam {

    /**
     * 场次
     */
    private String session;

    /**
     * 场次的后一个小时
     */
    private String nextHour;
}
