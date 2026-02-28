package com.shop.cereshop.app.param.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BroadCastParam {

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("拼团id")
    private Long shopGroupWorkId;

}
