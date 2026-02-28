package com.shop.cereshop.app.param.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("通用参数")
public class CommonIdParam {

    @ApiModelProperty("参数id")
    private Long id;

}
