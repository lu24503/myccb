/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.buyer;

import com.shop.cereshop.commons.domain.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 银行卡参数
 */
@Data
@ApiModel(value = "BankParam", description = "银行卡参数")
public class BankParam extends PageParam {

    /**
     * 银行卡id
     */
    @ApiModelProperty(value = "银行卡id")
    private Long bankId;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Long buyerUserId;
}
