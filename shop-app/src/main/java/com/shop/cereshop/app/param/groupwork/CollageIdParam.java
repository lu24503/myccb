/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.param.groupwork;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 邀请好友拼单参数
 */
@Data
@ApiModel(value = "CollageIdParam", description = "邀请好友拼单参数")
public class CollageIdParam {

    /**
     * 拼单id
     */
    @ApiModelProperty(value = "拼单id")
    private Long collageId;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

}
