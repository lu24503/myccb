/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.express;

import com.shop.cereshop.commons.poi.IsNeeded;
import lombok.Data;

/**
 * 导入快递公司编码数据
 */
@Data
public class ExpressImort {

    /**
     * 快递公司
     */
    @IsNeeded
    private String company;

    /**
     * 快递公司编码
     */
    @IsNeeded
    private String code;
}
