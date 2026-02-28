package com.shop.cereshop.app.param.credit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("积分签到查询参数")
public class CreditSignInParam {

    @ApiModelProperty("月份(格式:yyyy-MM)")
    private String month;

}
