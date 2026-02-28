/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.distributor;

import com.shop.cereshop.commons.domain.buyer.CereBuyerWithdrawal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户信息
 */
@Data
@ApiModel(value = "Distributor", description = "账户信息")
public class Distributor {

    /**
     * 账户可提现金额
     */
    @ApiModelProperty(value = "账户可提现金额")
    private BigDecimal price;

    /**
     * 提现历史记录
     */
    @ApiModelProperty(value = "提现历史记录")
    private List<CereBuyerWithdrawal> withdrawals;
}
